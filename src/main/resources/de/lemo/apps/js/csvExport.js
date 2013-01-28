(function(dataExport, undefined) {

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

  function createRecord(data, line, converter) {
    var record = '';
    for ( var i = 0; i < converter.length; i++) {
      var value = converter[i](data, line);
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

    // records
    for ( var i = 0; i < recordCount; i++) {
      result += createRecord(data, i, converter);
    }
    // remove last newline
    result = result.replace(/\n$/, '');
    return result;
  };

})(window.dataExport = window.dataExport || {});
