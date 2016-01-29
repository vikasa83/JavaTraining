var request = require('request');
var async = require('async');

exports.handler = function(req, res) {
  async.parallel([
    /*
     * First external endpoint
     */
    function(callback) {
      var productId = req.param('productId');
      console.log(productId);
      var url = "http://localhost:8080/products/"+productId;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        callback(false, obj);
      });
    },
    /*
     * Second external endpoint
     */
    function(callback) {
      var productId = req.param('productId');
      var url = "http://localhost:9090/productprices/"+productId;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        callback(false, obj);
      });
    },
  ],
  /*
   * Collate results
   */
  function(err, results) {
    if(err) { console.log(err); res.send(500,"Server Error"); return; }
    res.send({productInfo:results[0], priceInfo:results[1]});
  }
  );
};


exports.batchHandler = function(req, res) {
    var productArray = [];
    var priceArray = [];
  async.waterfall([
      function(callback) {
        var productType = req.param('productType');
        //console.log(productType);
        var url = "http://localhost:8080/products?productType="+productType;
        request(url, function(err, response, body) {
        // JSON body
          if(err) { console.log(err); callback(true); return; }
          obj = JSON.parse(body);
          productArray = obj;
          callback(false, obj);
        });
      },
      function(arg1,callback) {
        console.log('arg1');
        console.log(arg1);
      //  var productType = req.param('productType');
        //console.log('productArray.length == '+productArray.length)
        for(var i = 0; i< productArray.length; i++){
          //console.log(productArray[i]);
          //console.log(productArray[i].productId);
          var url = "http://localhost:9090/productprices/"+productArray[i].productId;
           request(url, function(err, response, body) {
           // JSON body
           if(err) { console.log(err); callback(true); return; }
           //console.log(url);
           obj = JSON.parse(body);
           //console.log(obj);
           priceArray.push(obj);
           if(i == priceArray.length){
            console.log(obj);
            callback(false, obj);
          }
        });
        }
      }
    ],
    function (err, results) {
      if(err) { console.log(err); res.send(500,"Server Error"); return; }
      console.log('=====');
      console.log(results);
      //console.log(priceArray.length);
      //console.log('======');
      //console.log(priceArray);
      //console.log('======');
      //var priceInfoArray = [];

      res.send({productInfo:productArray, priceInfo:priceArray});
    }
  );

};
