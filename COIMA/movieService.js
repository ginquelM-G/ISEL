const req = require('request')
const reqS = require('sync-request')
const API_KEY = "b531994cdfaa8e3b441e4086b1c6756d";

const movieSearch = require('./model/MovieSearchDto')
const movieDetails = require('./model/MovieDetailsDto')
const movieCredits = require('./model/CreditsDto')

var details =[];


//getMoviesByName('Batman', () =>{})

module.exports = {
    getMoviesByName,
    getMoviesDetails,
    getPersonMovieDetails
}

//1. https://api.themoviedb.org/3/search/movie?api_key=*****&name=war+games
function getMoviesByName(name, cb){
    const URI = `https://api.themoviedb.org/3/search/movie?api_key=${API_KEY}&query=${name}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        //const movie = JSON.parse(data.toString())
        const movie = new movieSearch(JSON.parse(data.toString()))
        console.log(`URI: ${URI}`)
        cb(null, movie) 
    })
}


//2. https://api.themoviedb.org/3/movie/860?api_key=*****
function getMoviesDetails(movieId, cb){
    const URI = `https://api.themoviedb.org/3/movie/${movieId}?api_key=${API_KEY}`;
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        
        console.log(`URI: ${URI}`)
        const dt = JSON.parse(data.toString())
        cb(null, dt)
    })

   
}


// 3. https://api.themoviedb.org/3/movie/860/credits?api_key=*****
function getMoviesCredits(movieId,cb){
    const URI = `https://api.themoviedb.org/3/movie/${movieId}/credits?api_key=${API_KEY}`;

    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movieCreditsJson = JSON.parse(data.toString())
        credits = movieCredits(movieCreditsJson)
        console.log(`URI: ${URI}`)
        cb(null, movieCredits)
    })
   // return details
}



// 4. https://api.themoviedb.org/3/person/4756/movie_credits?api_key=*****
function getPersonMovieDetails(actorId, cb){
   // const URI = `https://api.themoviedb.org/3/movie/${actorId}/credits?api_key=${API_KEY}`;
   const URI = `https://api.themoviedb.org/3/person/${actorId}/movie_credits?api_key=${API_KEY}`
     req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movieCredits = JSON.parse(data.toString())
        console.log(`URI: ${URI}`)
        //console.log(movie.results[0])
        cb(null, movieCredits)
    })
}



// 5. https://api.themoviedb.org/3/person/8891?api_key=*****
function getPersonDetails(actorId, cb){
    const URI = `https://api.themoviedb.org/3/movie/${movieId}/credits?api_key=${API_KEY}`;
    //const URI = `https://api.themoviedb.org/3/person/${actorId}?api_key=${API_KEY}`
    req(URI, (err, resp, data) =>{
        if(err) return cb(err)
        const movieCredits = JSON.parse(data.toString())
        console.log(`URI: ${URI}`)
        //console.log(movieCredits.cast[0].toString())
        cb(null, movieCredits)
    })
}
