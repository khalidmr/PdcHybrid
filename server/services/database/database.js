/**
 * This file create and initialize the database
 */

var mongoose = require('mongoose')

//Try to get environments variable or define to localhost by default
var uri =
    process.env.MONGOLAB_URI ||
    process.env.MONGOHQ_URL ||
    'mongodb://localhost/LinkedTree';

var port = process.env.PORT || 5000

mongoose.connect(uri, function (err, res) {
    if (err)
        console.log ('ERROR connecting to: ' + uri + '. ' + err)
    else
        console.log ('Succeeded connected to: ' + uri)
})

/**
 * Export variables
 */
module.exports.mongoose = mongoose