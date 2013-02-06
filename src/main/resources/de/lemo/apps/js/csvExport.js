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
    var result = '';

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

  dataExport.barChartButton = function(selector, data, chart, visibleOnly) {

    $(selector).click(function() {
      var series = data()[0];
      var header = [];
      var converter = [];

      header[0] = "Course";
      converter[0] = function(d, row) {
        return d[0].values[row].x;
      };

      if (visibleOnly) {
        data = data.filter("not:[disabled=true]");
      }

      for ( var i = 0; i < series.length; i++) {
        header.push(series[i].key);
        converter.push(function(d, row, column) {
          return d[column - 1].values[row].y;
        });
      }

      var csvData = dataExport.createCSV(series, series[0].values.length, converter, header);
      console.log(csvData);
    });
  };

  dataExport.lineWithFocusChartButton = function(selector, data, chart, visibleOnly) {
    $(selector).click(function() {

      var series = data[0];
      var header = [];
      var converter = [];

      var getX = chart.lines.x();
      var getY = chart.lines.y();


      // filtering
      if (visibleOnly) {
        // filter visibility
        series = _.filter(series, function(e) {
          return !e.disabled;
        });
        // filter range
        var min = chart.xAxis.domain()[0];
        var max = chart.xAxis.domain()[1];
        _.each(series, function(element) {
          element.values = _.filter(element.values, function(e) {
            return getX(e) >= min && getX(e) <= max;
          })
        })
      }

      // provide x axis value, it is the same for all series
      header[0] = "Timestamp";
      converter[0] = function(d, i) {
        return getX(d[0].values[i]);
      };

      // provide y axis values for each series
      function getValue(i) {
        return 
      }
      _.each(series, function(element, index) {
        header.push(series[i].key);
        converter.push(function(d, i) {
          return getY(d[index].values[j]);
        };);
      });

      var csvData = dataExport.createCSV(series, series[0].values.length, converter, header);
      console.log(csvData);
    });
  };

})(window.dataExport = window.dataExport || {}, window.jQuery, T5._);
