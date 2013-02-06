(function(d3custom, $, undefined) {

  function csvExport(data) {

    dataExport.barChartButton('.export-button', data, null, false);
  }

  d3custom.run = function() {

    var data = d3custom.data;

    if (!data) {
      $("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
      return;
    }

    var dataRequests = data[0].values;
    dataUser = data[1].values;

    console.log("Before Sort:" + dataRequests);

    dataRequests.sort(function(a, b) {
      return (b.y - a.y);
    });
    dataUser.sort(function(a, b) {
      return (b.y - a.y);
    });

    console.log("After Sort:" + dataRequests);


    nv.addGraph(function() {
      var chart = nv.models.multiBarChart().showControls(false).reduceXTicks(false);

      chart.xAxis.tickFormat(function(d) {
        return d;
      });

      chart.yAxis.tickFormat(d3.format(''));

      d3.select('#viz svg').datum(data).transition().duration(500).call(chart);
      d3.selectAll('.nv-x text').attr('transform', 'translate(0,5)rotate(45)').style('text-anchor', 'start');

      nv.utils.windowResize(chart.update);

      csvExport(d3.select('#viz svg'));

      return chart;
    });


  };


})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
