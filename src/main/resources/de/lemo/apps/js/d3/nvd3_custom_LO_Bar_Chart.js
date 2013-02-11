(function(d3custom, $, _) {

  d3custom.run = function() {

    var data = d3custom.data;

    if (!data) {
      // TODO there's some nvd3/d3 function for this case
      $("#viz").prepend($('<div class="alert">No matching data found. Please check your filter setting.</div>'));
      return;
    }

    var dataRequests = data[0].values;
    var dataUser = data[1].values;

    dataRequests.sort(function(a, b) {
      return (b.y - a.y);
    });
    dataUser.sort(function(a, b) {
      return (b.y - a.y);
    });

    nv.addGraph(function() {
      var chart = nv.models.multiBarChart().showControls(false).reduceXTicks(false);

      chart.yAxis.tickFormat(d3.format('d'));

      d3.select('#viz svg').datum(data).transition().duration(500).call(chart);
      d3.selectAll('.nv-x text').attr('transform', 'translate(0,5)rotate(45)').style('text-anchor', 'start');

      nv.utils.windowResize(chart.update);

      dataExport.barChartButton('.export-button', d3.select('#viz svg').data(), chart);

      return chart;
    });


  };


})(window.d3custom = window.d3custom || {}, jQuery, T5._);

$(document).ready(window.d3custom.run);
