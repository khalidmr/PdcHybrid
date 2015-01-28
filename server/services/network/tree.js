/*********************************
 * This file define all the services
 * we can use to make operations on
 * trees
 *********************************/
//Message and Tree are mongoose models defined in model.js
var Message = require(__base + 'services/database/model.js').Message
var Tree = require(__base + 'services/database/model.js').Tree

var utils = require(__base + 'services/utils/utils.js')

/**
 * Create a new tree
 */
module.exports.add = function(request,response) {

    var tree = {
        id    : request.body.id,
        name  : request.body.name,
        kind  : request.body.kind,
        specy    : request.body.specy,
        type  : request.body.type,
        trunk : request.body.trunk,
        crown : request.body.crown,
        height: request.body.height,
        remarquable       : request.body.remarquable,
        coordinates : request.body.coordinates,
        messages : []
    }

    //Check if the tree already exists in base
    Tree.find({id : tree.id}, function(err, obj) {

        if (obj.length > 0) {
            //If yes, just send back an error
            utils.httpResponse(response,403,'Tree already exists')
        }
        else {
            //If not, define a real Tree object (as define by mongoose)
            var tmpTree = new Tree ({
                id : tree.id,
                name : tree.name,
                kind : tree.kind,
                specy : tree.specy,
                type : tree.type,
                trunk : tree.trunk,
                crown : tree.crown,
                height : tree.height,
                remarquable : tree.remarquable,
                coordinates : tree.coordinates,
                messages : []
            });
            //Mongoose, please save my tree in your documents !
            tmpTree.save(function (err) {
                if (err)
                    //No, I don't want to !!
                    utils.httpResponse(response,500,err)
                else
                    //Of course I will ! And you're welcome !
                    utils.httpResponse(response,200,"The tree has been add to the database")
            });
        }
    });
}

/**
 * Get a tree by Id
 */
 module.exports.getId = function(request,response) {
    //Try to find the tree in the db
    Tree.findOne({id: request.body.treeId}, function(err,tree) {
        if(tree) {
            var tmpTree = {
                id : tree.id,
                name : tree.name,
                kind : tree.kind,
                specy : tree.specy,
                type : tree.type,
                trunk : tree.trunk,
                crown : tree.crown,
                height : tree.height,
                remarquable : tree.remarquable,
                coordinates : tree.coordinates,
            }
            utils.httpResponse(response,200,'Tree successfully found', tmpTree)
        }
        else {
            utils.httpResponse(response,500,'Tree not found')
        }
    })
}

/**
 * Delete a tree
 */
 module.exports.remove = function(request,response) {
    //Just remove a Tree from the database... RIP ;-(
    Tree.remove({id: request.body.treeId}, function(err,tree) {
        if(tree) {
            utils.httpResponse(response,200,'Tree successfully removed')
        }
        else {
            utils.httpResponse(response,500,'Tree not found')
        }
    })
}

/**
 * Get the tree at proximity
 */
module.exports.proximity = function(request, response){
    Tree.find({},function(err, trees) {
        //Sort the trees by distance from the user position
        trees.sort(function(x, y){
            return utils.distance(x.coordinates.lat, x.coordinates.long,request.body.lat,request.body.long) - utils.distance(y.coordinates.lat, y.coordinates.long,request.body.lat,request.body.long);
        });
        var index = 0;
        var result = []; //Contains the trees we'll send back
        //Take only the trees inside the radius limit.
        for(i=0; i<trees.length; i++){
            var dist = utils.distance(trees[i].coordinates.lat, trees[i].coordinates.long, request.body.lat, request.body.long);
            console.log(dist)
            if( dist > request.body.radius){
                //Since the array is sorted ascendantly, we can go outside the loop right after the first tree outside the limit
                //break;
            }
            else{
                //We send back a part of the tree : only relevent data to locate and identify it
                result.push({ id : trees[i].id, coordinates : {lat : trees[i].coordinates.lat, long : trees[i].coordinates.long}, distance: dist})
            }
        }

        if (result) {
            utils.httpResponse(response,200,'Trees successfully found',result)
        }
        else
            utils.httpResponse(response,500,'Trees not found')
    });
}


