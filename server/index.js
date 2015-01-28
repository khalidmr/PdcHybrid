/************************************
* This file define the server and start
* it. Then it call the file route.js
* to resolve URL and call the services
*************************************/
global.__base = __dirname + '/'

var express = require('express');
var bodyParser = require('body-parser') //Used to parse json in request body

//This is our server
var app = express();

//Let's make it accessible from everywhere
module.exports.app = app

app.set('port', (process.env.PORT || 5000));
app.use(express.static(__dirname + '/public'))
	.use(bodyParser.json());

//This route is useless and is set only to check if server is online
app.get('/', function(request, response) {
  response.send('_o/');
});

//Start the server
app.listen(app.get('port'), function() {
  console.log("Node app is running at localhost:" + app.get('port'));
});

//Call the file route.js to route the requests
var route = require('./services/route/routes.js')
