/*************************************
 * Just a utilities file like another
 *************************************/
/**
 * Return http response. Print message in console.
 * @param response
 * @param code Http status code
 * @param description Description of the response
 */
function httpResponse(response,code,description,content) {
    var s

    if(/^1\d+/.test(code)) {
        s = '[INFORMATION] '
        console.log(s + description)
    } else if(/^2\d+/.test(code)) {
        s = '[SUCCESS] '
        console.log(s + description)
    } else if(/^3\d+/.test(code)) {
        s = '[REDIRECTION] '
        console.log(s + description)
    } else if(/^4\d+/.test(code)) {
        s = '[CLIENT ERROR] '
        console.log(s + description)
    } else if(/^5\d+/.test(code)) {
        s = '[SERVER ERROR] '
        console.log(s + description)
    }

    response.writeHead(code, { 'Content-Type': 'application/json' });

    if(content === undefined)
        response.write(JSON.stringify({description : description}))
    else
        response.write(JSON.stringify({description : description,content : content}))

    response.end()
}

/**
 * A simple function to convert degre in radians
 */
function degToRad(deg) {
    return deg * (Math.PI / 180);
}

/**
 * Evaluate the distance between two spots with specified lattitude and longitude
 */
function distance(lat1,long1,lat2,long2) {
    var R = 6378137 /* Earth radius */
    lat1 = degToRad(lat1)
    lat2 = degToRad(lat2)
    long1 = degToRad(long1)
    long2 = degToRad(long2)
    var r1 = Math.pow(Math.sin((lat2-lat1)/2),2)
    var r2 = Math.pow(Math.sin((long2-long1)/2),2)
    return 2 * R * Math.asin(Math.sqrt(r1 + Math.cos(lat1) * Math.cos(lat2) * r2))
}

/**
 * Export functions
 */
module.exports.httpResponse = httpResponse
module.exports.distance = distance
