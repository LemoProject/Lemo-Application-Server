(function(dataExport, $, _, undefined) {

  function csvEscape(value) {
    value = '' + value;
    if (value.indexOf('"') != -1) {
      // needs escaping
      return '"' + value.replace(/"/g, '""') + '"';
    }
    return value;
  }

  function createHeader(columnNames) {
    var record = '';
    for ( var i = 0; i < columnNames.length; i++) {
      record += (i == 0 ? '' : ',') + csvEscape(columnNames[i]);
    }
    return record + '\n';
  }

  function createRecord(data, row, converter) {
    var record = '';
    for ( var i = 0; i < converter.length; i++) {
      var value = converter[i](data, row, i);
      record += (i == 0 ? '' : ',') + csvEscape(value);
    }
    return record + '\n';
  }

  /**
   * Convert a data object of arbitrary format to CSV records using converter
   * functions.
   * 
   * @param data
   *          the data to convert to CSV, in any suitable format
   * @param recordCount
   *          number of records (excluding the optional header record)
   * @param converter
   *          array of functions with signature f(data:?, row:Int)->String to
   *          create a value based on the data and current row count
   * @param columnNames
   *          array of column names used as header record
   */
  dataExport.createCSV = function(data, recordCount, converter, columnNames) {
    var result = '\uFEFF'; // Excel needs a BOM
    // header record
    if (columnNames && columnNames.length) {
      result += createHeader(columnNames);
    }
    // data records
    for ( var i = 0; i < recordCount; i++) {
      result += createRecord(data, i, converter);
    }
    // remove last newline
    result = result.replace(/\n$/, '');
    return result;
  };


  /**
   * Open a modal window when the element specified by the selector is clicked.
   * The modal contains option to download CSV files containing all data or
   * currently visible data only (depending on the visualization).
   * 
   * @param exportFunction
   *          f(data, chart, visibleOnly)->csv:String
   */
  function createModalExportOptions(selector, exportFunction, data, chart) {
    // TODO recreate as reusable tml template?
    // TODO Use better file name, like course + current date
    var button = $(selector);
    var exportModal = $('<div class="modal hide fade">' + '<div class="modal-header"><h3>CSV Export</h3></div>'
        + '<div class="modal-body">' + '<p>Choose the data to download as CSV file.</p>'
        + '<a download="data.csv" id="data-export-visible" class="btn">Currently visible data</a><br/>'
        + '<a download="data.csv" id="data-export-all" class="btn">All loaded data</a>' + '</div>'
        + '<div class="modal-footer"><a href="#" class="btn" data-dismiss="modal">Close</a></div>' + '</div>');
    exportModal.insertAfter(button);
    button.click(function() {
      exportModal.modal();
    });

    exportModal.find('#data-export-visible').click(function() {
      var csv = exportFunction(data, chart, true);
      this.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv);
      exportModal.modal('hide');
    });

    exportModal.find('#data-export-all').click(function() {
      if (!this.href) {
        var csv = exportFunction(data, chart, false);
        this.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv);
      }
      exportModal.modal('hide');
    });

    return exportModal;
  }

  /*
   * private visualization specific export functions
   */

  function exportLOBarChart(data, chart, visibleOnly) {
    var series = data[0];
    var header = [];
    var converter = [];
    var getX = chart.x();
    var getY = chart.y();

    // filtering
    if (visibleOnly) {
      // filter series visibility
      series = _.filter(series, function(e) {
        return !e.disabled;
      });
    }

    header[0] = "Learning Object";
    converter[0] = function(d, row) {
      return getX(d[0].values[row]);
    };

    // provide y axis values for each series
    _.each(series, function(element, index) {
      header.push(series[index].key);
      converter.push(function(d, i) {
        return getY(d[index].values[i]);
      });
    });

    var csv = dataExport.createCSV(series, series[0].values.length, converter, header);
    console.log(csv);
    return csv;
  }


  function exportUsageLineWithFocusChart(data, chart, visibleOnly) {
    var series = data[0];
    var header = [];
    var converter = [];
    var getX = chart.lines.x();
    var getY = chart.lines.y();

    // filtering
    if (visibleOnly) {
      // filter series visibility
      series = _.filter(series, function(e) {
        return !e.disabled;
      });
      // filter range
      var min = chart.xAxis.domain()[0];
      var max = chart.xAxis.domain()[1];
      _.each(series, function(element) {
        element.values = _.filter(element.values, function(e) {
          return getX(e) >= min && getX(e) <= max;
        });
      });
    }

    // provide x axis value, it is the same for all series
    header[0] = "Date";
    converter[0] = function(d, i) {
      var timestamp = getX(d[0].values[i]);
      var format = d3.time.format("%Y-%m-%d");
      return format(new Date(timestamp));
    }

    // provide y axis values for each series
    _.each(series, function(element, index) {
      header.push(series[index].key);
      converter.push(function(d, i) {
        return getY(d[index].values[i]);
      });
    });

    var csv = dataExport.createCSV(series, series[0].values.length, converter, header);
    console.log(csv);
    return csv;
  }

  /*
   * public button creation functions
   */

  /**
   * Export a nvd3 bar chart to CSV.
   * 
   * @param selector
   *          export button selector
   * @param data
   *          data object to export as csv
   * @param chart
   *          nvd3 chart
   */
  dataExport.barChartButton = function(selector, data, chart) {
    createModalExportOptions(selector, exportLOBarChart, data, chart);
  };

  /**
   * Export a nvd3 bar line chart to CSV.
   * 
   * @param selector
   *          export button selector
   * @param data
   *          data object to export as csv
   * @param chart
   *          nvd3 chart
   */
  dataExport.lineWithFocusChartButton = function(selector, data, chart) {
    createModalExportOptions(selector, exportUsageLineWithFocusChart, data, chart);
  };

})(window.dataExport = window.dataExport || {}, window.jQuery, T5._);
