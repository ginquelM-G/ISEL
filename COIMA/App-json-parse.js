const req = require('request')
const API_KEY = "b531994cdfaa8e3b441e4086b1c6756d";

//getLeagueLeader(445)
// sleepFor(2000)


getMoviesByName('Batman')

//1. https://api.themoviedb.org/3/search/movie?api_key=*****&name=war+games
function getMoviesByName(name, cb){
    const URI = `https://api.themoviedb.org/3/search/movie?api_key=${API_KEY}&query=${name}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movie = JSON.parse(data.toString())
        console.log(movie.results[0])
    })
}

function getMoviesByNameCb(err, resp, data){
    if(err) throw err
    const movie = JSON.parse(data.toString())
    console.log(movie.results[0])
}


function sleepFor( sleepDuration ){
    var now = new Date().getTime();
    while(new Date().getTime() < now + sleepDuration){ /* do nothing */ } 
}