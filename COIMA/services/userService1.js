var request = require('request');

var headers = {
    'content-type': 'application/json'
};

var dataString = {
    "username": "ginquel",
    "password": "123",
    "fullname": "Ginquel Moreira",
    "classics":[],
    "best_all_time":[],
    "imdb_most_rated":[],
    "to_see":[],
    "seen":[]
}

var options = {
    url: 'http://127.0.0.1:5984/movies/ginquel5',
    method: 'POST',
    headers: headers,
    body: JSON.stringify(dataString)
};

function callback(error, response, body) {
    if (!error && response.statusCode == 200) {
        console.log(body);
    }
    console.log(response.statusCode)
}


function x(){
  request(options, callback);  
}

x()