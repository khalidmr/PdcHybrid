/*************************************
* This file define all the services (well, one in fact...)
* we can use to manipulate messages
**************************************/

//Message and Tree schema ad define in model.js
var Message = require(__base + 'services/database/model.js').Message
var Tree = require(__base + 'services/database/model.js').Tree

var utils = require(__base + 'services/utils/utils.js')

/**
 * Create a new tree
 */
module.exports.add = function(request,response) {

    var message = {
        sender    : request.body.sender,
        content  : request.body.content,
    }

    //We first check if the tree in which we want to write exists
    Tree.findOne({id: request.body.treeId}, function(err, tree){
        if (tree) {
            //If yes, create the message and save it
            var tmpMessage = new Message ({
                sender : message.sender,
                content : message.content,
            });

            tmpMessage.save(function (err, message) {
                if (err){
                    utils.httpResponse(response,500,err)
                }
                else{
                    //But don't forget to add the message to the tree and save the latter !
                    tree.messages.push(message)
                    tree.save()
                    utils.httpResponse(response,200,"The message has been add to the database")
                }
            });
        }
        else {
            utils.httpResponse(response,500,'Tree does not exist')
        }
    })
}

/**
 * Function to send all messages of a Tree specifid in parameter
 */
module.exports.getAllOfTree = function(request, response){
    Tree.findOne({id : request.body.treeId}, function(err, tree){
        if(tree){
            if(tree.messages.length > 0){
                Message.find({_id : {$in : tree.messages}}, '-__v', function(err, messages){
                    if(messages.length > 0){
                        utils.httpResponse(response,200,'Messages successfully found', messages)
                    }
                    else{
                        utils.httpResponse(response,201,'No messages on this tree')
                    }
                });
            }
            else{
                utils.httpResponse(response,201,'No messages on this tree')
            }
        }
        else{
            utils.httpResponse(response,500,'Tree not found')
        }
    });
}

/**
 * Function to delete a message (not used in the application fr the moment)
 */
module.exports.remove = function(request, response){
    //Just remove a Message from the database.
    Message.remove({_id: request.body.postId}, function(err,message) {
        if(message) {
            Tree.findOne({id : request.body.treeId}, function(err, tree){
                if(tree){
                    var index = tree.messages.indexOf(message)
                    tree.messages.splice(index,1)
                    if(index > -1){
                        tree.save()
                    }
                    utils.httpResponse(response,200,'Message successfully removed')
                }
                else{
                    utils.httpResponse(response,500,'Cannot remove the message')   
                }
            });
        }
        else {
            utils.httpResponse(response,500,'Message not found')
        }
    })
}
