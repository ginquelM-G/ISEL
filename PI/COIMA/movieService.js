const req = require('request')
const API_KEY = "b531994cdfaa8e3b441e4086b1c6756d";

//getLeagueLeader(445)
// sleepFor(2000)


//getMoviesByName('Batman', () =>{})

module.exports = {
    getMoviesByName,
    getMoviesDetails
}

//1. https://api.themoviedb.org/3/search/movie?api_key=*****&name=war+games
function getMoviesByName(name, cb){
    const URI = `https://api.themoviedb.org/3/search/movie?api_key=${API_KEY}&query=${name}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movie = JSON.parse(data.toString())
        console.log(movie.results[0])
        cb(null, movie)
    })
}


//2. https://api.themoviedb.org/3/movie/860?api_key=*****
function getMoviesDetails(movieId, cb){
    const URI = `https://api.themoviedb.org/3/movie/${movieId}?api_key=${API_KEY}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movieDetails = JSON.parse(data.toString())
        console.log('id: '+movieDetails.id + '\nTitle: ' + movieDetails.title)
        cb(null, movieDetails)
    })
}


// 3. https://api.themoviedb.org/3/movie/860/credits?api_key=*****
function getMoviesDetailsss(movieId, cb){
    const URI = `https://api.themoviedb.org/3/movie/${movieId}/credits?api_key=${API_KEY}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movieCredits = JSON.parse(data.toString())
        //console.log(movie.results[0])
        cb(null, movieCredits)
    })
}



// 4. https://api.themoviedb.org/3/person/4756/movie_credits?api_key=*****
function getPersonMovieDetails(actorId, cb){
    const URI = `https://api.themoviedb.org/3/movie/${actorId}/credits?api_key=${API_KEY}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movieCredits = JSON.parse(data.toString())
        //console.log(movie.results[0])
        cb(null, movieCredits)
    })
}



// 5. https://api.themoviedb.org/3/person/8891?api_key=*****
function getPersonDetails(movieId, cb){
    const URI = `https://api.themoviedb.org/3/movie/${movieId}/credits?api_key=${API_KEY}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movieCredits = JSON.parse(data.toString())
        //console.log(movie.results[0])
        cb(null, movieCredits)
    })
}
/** 
function getMoviesByNameCb(err, resp, data){
    if(err) throw err
    const movie = JSON.parse(data.toString())
    console.log(movie.results[0])
}


function sleepFor( sleepDuration ){
    var now = new Date().getTime();
    //while(new Date().getTime() < now + sleepDuration){  } 
}
*/