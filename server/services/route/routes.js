/*********************************
 * This file define all the possible routes
 * we can call to execute services
 *********************************/

//This is our server
var app = require(__base + 'index.js').app
//The files tree.js and message.js define all the services
var tree = require(__base + 'services/network/tree.js')
var message = require(__base + 'services/network/message.js')

/**
 * Trees management
 */
 //Get all trees in a specified radius from specified coordinates
app.post('/tree/proximity', function(request, response){
    tree.proximity(request, response);
})

//Return the tree with the specified id
app.post('/tree/id', function(request, response){
    tree.getId(request, response);
})

//Delete a tree from the database
app.post('/tree/delete', function(request, response){
    tree.remove(request, response)
})

//Add a tree to the database
app.post('/tree/add', function(request, response){
    tree.add(request, response)
})

/**
 * Messages management
 */
 //Add a message to the database, linked to a specified tree
 app.post('/message/new', function(request, response){
    message.add(request, response)
})

app.post('/message/delete', function(request, response){
    message.remove(request, response)
})

app.post('/message/treeId', function(request, response){
    message.getAllOfTree(request, response)
})


