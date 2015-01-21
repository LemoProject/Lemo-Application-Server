(function(d3custom, $, undefined) {

  var pollingIntervall = 3000;

  d3custom.run = function() {

    if (d3custom.pathType == "viger") {
      console.log("viger analysis");
      drawGraph(d3custom.resultData);
      
    } else if (d3custom.pathType == "bide") {
      console.log("bide analysis");
      $("#pagination").hide();

      // get the zone update url to communicate with tapestry
      var resultUrl = $('#refreshZone').attr('href');

      var pollResult = function() {
        console.log("polling result from ", resultUrl);
        $.ajax({
          url : resultUrl,
          error : function(xhr, textStatus, errorThrown) {
            // TODO show some error
            console.log(textStatus, xhr);

          },
          success : function(data) {
            console.log("status", data.status);
            if (data.status != 200) {
              // 202 accepted or some error?
              console.log("result not yet ready");
              // try again later
              setTimeout(pollResult, pollingIntervall);
            } else {
              console.log("data", data);
              d3custom.spinner.stop();
	      	  if (data.bideResult.nodes.length==0) {
	    			$("#viz").prepend($('<div class="alert">No result. Please change your filter setting.</div>'));
	    			return;
	    	  }
              drawGraph(data.bideResult);
            }
          }
        });
      }
      setTimeout(pollResult, pollingIntervall);
    } else {
      console.log("pathType is invalid: ", d3custom.pathType);
    }
  }

  /**
   * Same implementation for bide and viger.
   */
  function drawGraph(data) {
    var w = $("#viz").width(), h = 500, amt = 800, maxPos = 11, fill = hashColor(), nodes = [], links = [], foci = [];
    var page = 1, pages = 1, maxLength = 1;


    var _nodes = data.nodes, _links = data.links;


    // Bind slide event to update minSup Value in front end
    $("#minSup-slider").bind("slide", function(event, ui) {
      $("#minSupSlider-label").html("Minimum Support (0.1 - 1): " + ui.value / 10);
    });

    // Bind slide event to update pathlength Value in front end
    $("#pathLength-slider").bind("slide", function(event, ui) {
      $("#pathLengthSlider-label").html("Path Length (1 - 200): " + ui.values[0] + " - " + ui.values[1]);
    });

    // check if we have values to work with
    if (!_nodes || !_links) {
      $("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
      return;
    }

    function calculate() {
      var amtPaths = _nodes[_nodes.length - 1].pathId;
      var i = +amtPaths;
      // pages = 0
      while (i > 24) {
        i = amtPaths / ++pages;
      }
      amt = Math.round(i + 0.5);
      console.log("AMT SIZE: " + amt)
    }

    calculate();

    $("#pages").html('' + page + "/" + pages);

    if (pages > 1)
      $("#pagination").show();
    if (page == 1)
      $("#prev").hide();
    if (page == pages)
      $("#next").hide();

	
    function init() {
      console.log("Bin im Init - Starting Node Init");
      var posCounter = 1;
      var skippedCounter = 0;

      $.each(_nodes, function(i, v) {
        if (v.pathId < amt * (page - 1) + 1) {
          skippedCounter++;
        }

        if (v.pathId < (page - 1) * amt + amt + 1 && v.pathId >= (page - 1) * amt + 1) {
          if (i >= 1 && _nodes[i - 1].pathId == _nodes[i].pathId) {
            posCounter++;
          } else {
            if (posCounter > maxLength)
            	maxLength = posCounter;
            posCounter = 1;
            // console.log("Neuer Pfad");
          }
          // console.log("Schreibe Node "+i+" Pfad: "+v.pathId+" Position:
          // "+posCounter+" Name:"+v.title);


          nodes.push({
            "id" : i,
            "pid" : v.pathId,
            "pos" : posCounter,
            "name" : v.title,
            "displayname" : v.title,
            "value" : v.value,
            "type" : v.type,
            "totalRequests" : v.totalRequests,
            "totalUsers" : v.totalUsers,
            "nameprepared" : false,
            "selected" : false
          });
        } else {
          // console.log("Max. number of pathes ("+amt+") reached ...!");
        }
      });
      console.log("Bin im Init - Finished Node Init, starting Link Init " + _links.length);

      if ($.isArray(_links)) {
        console.log("Handle Links as Array");
        $.each(_links, function(i, v) {
          if (v.pathId < (page - 1) * amt + amt + 1 && v.pathId >= (page - 1) * amt + 1) {
            links.push({
              "source" : nodes[v.source - skippedCounter],
              "target" : nodes[v.target - skippedCounter],
              "value" : v.value
            });
            // console.log("Schreibe Link: PathId
            // "+nodes[v.source-skippedCounter].pid+"("+v.source+"--"+v.target+")"+"
            // Source: "+nodes[v.source-skippedCounter].name+" Target:
            // "+nodes[v.target-skippedCounter].name);
          } else {
            // console.log("Max. number of pathes ("+amt+") reached ...!");
          }
        });
      } else {
        // TODO maybe some old, dead code
        console.log("Handle Links as Object");
        links.push({
          "source" : nodes[_links.source],
          "target" : nodes[_links.target],
          "value" : _links.value
        });
      }
      console.log("Bin im Init - Finished Link Init .... Leaving");

    }

    for ( var i = 0; i < amt; i++) {
      foci[i] = ({
        x : 20 + (w - 40) / (amt - 1) * (i),
        y : 20
      });
    }

    var vis = d3.select("#viz").append("svg:svg").attr("width", w).attr("height", h);

    var force = d3.layout.force().friction(.85).gravity(0).linkStrength(0).charge(0).size([w, h]).on("tick",
        function(e) {
          tick(e)
        });


    function tick(e) {
      // Push nodes toward their designated focus.
      var k = .1 * e.alpha;
      nodes.forEach(function(o, i) {
        // console.log("foci: "+(+amt*((+pages)-(+page)+1)-o.pid)+"
        // PID:"+o.pid+"
        // AMT:"+amt);
        o.y += (foci[o.pid - (page - 1) * amt - 1].y - o.y + (o.pos - 1) * 350 / maxPos + 20) * k;
        o.x += (foci[o.pid - (page - 1) * amt - 1].x - o.x) * k;
      });


      vis.selectAll("g.node").attr("transform", function(d) {
        return "translate(" + d.x + "," + d.y + ")"
      });

      vis.selectAll("line.link").attr("x1", function(d) {
        return d.source.x;
      }).attr("y1", function(d) {
        return d.source.y;
      }).attr("x2", function(d) {
        return d.target.x;
      }).attr("y2", function(d) {
        return d.target.y;
      });
    }
    ;
    function greyout(color) {
      var newC = 0.3 * color.r + 0.6 * color.g + 0.1 * color.b;
      return d3.rgb(r = newC, g = newC, b = newC);
    }


    nodes = force.nodes(), links = force.links();

    init();
    
    var height = h;
    if (h < maxLength*26.5+70) 
    	height = maxLength*26.5+70;
    
    d3.select("#viz svg").attr("height", height);

    var pathAmount = [];

    function restart() {

      var link = vis.selectAll("line.link").data(links);

      link.enter().append("svg:line").attr("class", "link").style("stroke-width", "1").style("stroke", "#000");

      link.exit().remove();

      var node = vis.selectAll("g.node").data(nodes, function(d) {
        return d.id;
      });

      node.exit().remove();

      node.selectAll("circle").attr("r", 8).style("fill", function(d) {
        return hashColor(d.name);
      }).style("stroke", function(d) {
        return d3.rgb(d3.scale.category20(1)).darker(2);
      }).style("stroke-width", 1.5)

      node.selectAll("text").text(function(d) {
        return d.name
      });

      var nodeEnter = node.enter().append("svg:g").attr("class", "node");

      nodeEnter.append("circle").attr("class", "node").attr("r", 8).attr("title", function(d) {
        return d.name;
      }).attr("cx", function(o) {
        return 0;
      }).attr("cy", function(o) {
        return 0;
      }).on("click", function(d) {
    	console.log("d.selected:" + d.selected);
        if (d.selected == true) {
          for ( var i = 0; i < foci.length; i++) {
            foci[i] = ({
              x : 20 + (w - 40) / (amt - 1) * (i),
              y : 20
            });
          }
          d3.selectAll("g.node circle").style("fill", function(o, i) {
            return hashColor(o.name);
          });

          d3.selectAll("g.node text").text(function(o, i) {
            return "";
          });
          force.start();
          d.selected = false;
        } else {

          d3.selectAll("g.node circle").style("fill", function(o, i) {
            return hashColor(o.name);            
            // Alternative version with greyout of non focused resources
            // o.pid != d.pid ? greyout(d3.rgb(fill(o.name))) :
            // fill(o.name)
          });


          nodes.forEach(function(o, i) {
            if (o.pid == d.pid) {
              o.selected = true;
            } else {
              o.selected = false;
            }
          });

          var nameMaxLength = 40;

          d3.selectAll("g.node text").text(function(o, i) {
            if (o.selected) {

              if (!o.nameprepared) {
                if (o.name.length < nameMaxLength) {
                  if (o.pid % amt > amt / 2) {
                    // last row on page
                    while (o.displayname.length < nameMaxLength) {
                      // prepend with en-space (space with the size half of an
                      // em)
                      // TODO very inaccurate, better use text anchor/align
                      // instead
                      o.displayname = "\u2002".concat(o.displayname);
                    }
                  }
                } else {
                  // truncate long names with ellipsis
                  o.displayname = o.displayname.slice(0, nameMaxLength).concat(" …");
                }

                o.nameprepared = true;
              }
              console.log(o.displayname)
              return o.displayname;
            } else
              return "";
          });

          for ( var i = 0; i < amt; i++) {
            foci[i] = {
              x : 20 + (w - 40) / (amt - 1) * (i),
              y : 20
            };
          }


          var p = d.pid - (amt * (page - 1)) - 1;
          if (p >= amt / 2) {
            for ( var i = 0; i < p; i++) {
              foci[i].x = 20 + (w - 240) / (amt - 1) * (i);
            }
            for ( var i = p; i < amt; i++) {
              foci[i].x = 20 + (w - 240) / (amt - 1) * (i) + 200;
            }
          } else {
            for ( var i = 0; i < p + 1; i++) {
              foci[i].x = 20 + (w - 240) / (amt - 1) * (i);
            }
            for ( var i = p + 1; i < amt; i++) {
              foci[i].x = 20 + (w - 240) / (amt - 1) * (i) + 200;
            }
          }
          force.start();
        }
      }).style("fill", function(d) {
        return hashColor(d.name);
      }).style("stroke", function(d) {
        return d3.rgb(d3.scale.category20(1)).darker(2);
      }).style("stroke-width", 1.5);

      nodeEnter.append("svg:text").attr("class", "nodetext").attr("dx", function(d) {
        // TODO add some comments about what's happening here
        return d.pid - (amt * (page - 1)) - 1 >= amt / 2 ? -40 * 6.5 : "1.5em";
      }).attr("dy", ".3em").text(function(d) {
        return ""
      });


      nodeEnter.append("nodetitle").text(function(d) {
        return "<b>Support</b>: " + d.value
      });


      force.start();
    }


    // $('#minSup2-slider').slider({
    //
    // range: false,
    // min : 1,
    // max : 10,
    // value: 10,
    // slide : function( event, ui ) {
    // $( "#minSupSlider2-label" ).html( "Minimum Support (0.1 - 1): " +
    // ui.value );
    //
    //
    // console.log("MinSup Value: "+ui.value);
    // }
    // });
    //


    $('nodetitle').parent().tipsy({
      gravity : 'sw',
      html : true,
      title : function() {
        return $(this).find('nodetitle').text();
      }
    });

    next = function(bool) {
      if (bool) {
        page++;
      } else {
        page--;
      }
      if (page == 1)
        $("#prev").hide();
      else
        $("#prev").show();
      if (page == pages)
        $("#next").hide();
      else
        $("#next").show();
      $("#pages").html('' + page + "/" + pages);
      nodes = [];
      links = [];
      init();
      force.nodes(nodes);
      force.links(links);
      for ( var i = 0; i < amt; i++) {
        foci[i] = {
          x : 20 + (w - 40) / (amt - 1) * (i),
          y : 20
        };
      }
      restart();
      d3.selectAll("g.node text").text(function(o, i) {
        return "";
      });
    }

    restart();

  }
  ;


})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
