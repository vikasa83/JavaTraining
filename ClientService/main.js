var request = require('request');
var async = require('async');
var express = require('express');
const client = require('./clientservice.js');
var app = express();
app.get('/getById',function(req,res){
  client.handler(req,res);
});
app.get('/getByType',function(req,res){
  client.batchHandler(req,res);
});
app.listen(3000,function(){
  console.log('Example app listening on port 3000');
});
