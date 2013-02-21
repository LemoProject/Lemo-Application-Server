describe("Export", function() {

  describe("to CSV", function() {

    /*
     * Sample data in the same format as course activity / nvd3 multibar chart.
     * Each object in `data` represents an x,y value pair (timestamp, value) for
     * a single bar in the chart.
     */
    var data = [{
      key : "course-activity",
      values : [[1234567891, 11], [1234567892, 22], [1234567893, 33]]
    }, {
      key : "course-users",
      values : [[1234567891, 1], [1234567892, 2], [1234567893, 3]]
    }];

    /* CSV will have a line for each timestamp */
    var recordCount = data[0].values.length;

    /* CSV output will be a table: | timestamp | activity | users | */
    var date = function(data, line) {
      return data[0].values[line][0];
    };
    var activity = function(data, line) {
      return data[0].values[line][1];
    };
    var users = function(data, line) {
      return data[1].values[line][1];
    };


    it("create CSV without a headers", function() {
      var csv = window.dataExport.createCSV(data, recordCount, [date, activity, users]);
      expect(csv).toEqual("1234567891,11,1\n" + "1234567892,22,2\n" + "1234567893,33,3");
    });

    it("create CSV with a header record", function() {
      var csv = window.dataExport.createCSV(data, recordCount, [date, activity, users], ["Date", "Activity", "Users"]);
      expect(csv).toEqual("Date,Activity,Users\n" + "1234567891,11,1\n" + "1234567892,22,2\n" + "1234567893,33,3");
    });

    it("don't break on null values", function() {
      var csv = window.dataExport.createCSV();
      expect(csv).toEqual('');
    });

    it("don't break on empty values", function() {
      var csv = window.dataExport.createCSV({}, 0, [], []);
      expect(csv).toEqual('');
    });

    it("escape quotes in values", function() {
      var csv = window.dataExport.createCSV({}, 1, [function() {
        return 'some "value" with quotes';
      }]);
      expect(csv).toEqual('"some ""value"" with quotes"');
    });

    it("escape quotes in headers", function() {
      var csv = window.dataExport.createCSV({}, 0, [], ['some "header" with quotes']);
      expect(csv).toEqual('"some ""header"" with quotes"');
    });

  });

});
