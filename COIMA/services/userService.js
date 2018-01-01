'use strict'


/*
* Array of User objects
*/

const dbUsers = 'http://127.0.0.1:5984/movies/'
const nano = require('nano')('http://localhost:5984/')
/* var db_name = 'movies' 
var moviesDB = nano.use(db_name) */

const request = require('request')

module.exports ={
    'find': find,
    'authenticate': authenticate,
    'save': save,
    'createNewFileInCouchDB': createNewFileInCouchDB
}



function find(username, cb){
    const path = dbUsers + '/' +username
    request(path, (err, resp, body) =>{
        if(err) return cb(err)
        cb(null, JSON.parse(body))
    })
}


/**
 * @param String username 
 * @param String passwd 
 * @param Function cb callback (err, user, info) => void. If user exists
 * but credentials fail then calls cb with undefined user and an info message.
 */
function authenticate(username, passwd, cb){
    const path = dbUsers + '/' + username
    request(path, (err, resp, body)=>{
        if(err) return cb(err)
        if(resp.statusCode != 200) return cb(null, null, `User ${username} does not exists`)
        const user = JSON.parse(body)
        if(passwd != user.password) return cb(null, null, 'Invalid password')
        cb(null, user)
    })
}


// SAVE or Update
function save(user, cb){
    const path = dbUsers + '/' + user.username
    const options ={
        method: "PUT",
        Headers: {"Content-Type": "application/json"},
        body: JSON.stringify(user)
    }
    request(path, options, (err, res, body) =>{
        if(err) return cb(err)
        cb()
    })
}

/*
* Creating New Documents.
* Can be createt new documents by using the insert method..
*/
function createNewFileInCouchDB(reqBody, cb){
    var db_name = reqBody.username.toString() 
    console.log('body: %j', reqBody)
    const path = dbUsers + '/' + reqBody.username.toString()
    var options ={
        url: dbUsers + db_name,
        method: "PUT",
        Headers: {"Content-Type": "application/json"},
        json: true,
        //body: JSON.stringify(user)
        body: {
            "username": reqBody.username,
            "password": reqBody.password,
            "fullname": reqBody.fullname,
            "classics":[],
            "best_all_time":[],
            "imdb_most_rated":[],
            "to_see":[],
            "seen":[]
        }
    }
    request(dbUsers, function(err, resp, body) {
        // Read the document
        request.put(path, options, function(err, res, body) {
                if(err) cb(err)
                //console.log('### %j' + body)
                cb()
        })
        console.log('end callbakc')
    })
}