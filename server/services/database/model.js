/****************************
* This file define the schema
* for Tree and Message objects
*******************************/

var mongoose = require(__base + 'services/database/database.js').mongoose

/**
 * MongoDB schema
 */
var treeSchema = mongoose.Schema({
    id              : String,
    name            : String,
    kind            : String,
    specy   		: String,
    type            : String,
    trunk           : Number,
    crown           : Number,
    height          : Number,
    remarquable     : Boolean,
    coordinates     : {
        lat: Number,
        long: Number
    },
    messages        : [{
        type: mongoose.Schema.Types.ObjectId, ref: 'Message'
    }]
})

var messageSchema = mongoose.Schema({
    sender          : String,
    content         : String,
})

/**
 * Mongo model : we export the schema to use them in services files
 */
module.exports.Tree = mongoose.model('Tree', treeSchema)
module.exports.Message = mongoose.model('Message', messageSchema)
