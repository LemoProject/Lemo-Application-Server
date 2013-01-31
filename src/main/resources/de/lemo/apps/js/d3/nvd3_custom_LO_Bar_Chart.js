(function(d3custom, $, undefined) {

  function csvExport(data) {
    
    var headers = ["Course", data[0].key, data[1].key];
    var course = function(data, row) {
      return data[0].values[row].x;
    };
    var request = function(data, row) {
      return data[0].values[row].y;
    };
    var user = function(data, row) {
      return data[1].values[row].y;
    };

    // TODO i18n
    var exportButton = $('<div class="button export-button">Export to CSV</div>');
    $("#viz").append(exportButton);

    exportButton.click(function() {
      var csvData = dataExport.createCSV(data, data[0].values.length, [course, request, user], headers);
      console.log(csvData);
    });

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

    dataRequests.sort(function sortfunction(a, b) {
      return (b.y - a.y);
    });
    dataUser.sort(function sortfunction(a, b) {
      return (b.y - a.y);
    });

    console.log("After Sort:" + dataRequests);

    data[0].values = dataRequests;
    /**
     * causes the array to be sorted numerically and ascending
     */
    function sortfunction(a, b) {
      return (a.y - b.y);
    }

    nv.addGraph(function() {
      var chart = nv.models.multiBarChart().showControls(false).reduceXTicks(false);

      chart.xAxis.tickFormat(function(d) {
        return d;
      });

      chart.yAxis.tickFormat(d3.format(''));

      d3.select('#viz svg').datum(data).transition().duration(500).call(chart);

      nv.utils.windowResize(chart.update);

      d3.selectAll('.nv-x text').attr('transform', 'translate(0,5)rotate(45)').style('text-anchor', 'start');

      return chart;
    });


  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
