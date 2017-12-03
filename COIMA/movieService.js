const req = require('request')
const reqS = require('sync-request')
const API_KEY = "b531994cdfaa8e3b441e4086b1c6756d";

const checkRequest = require('./controlTheNumberOfRequests')
const MovieSearch  = require('./model/MovieSearchDto')
const MovieDetails = require('./model/MovieDetailsDto')
const MovieCredits = require('./model/CreditsDto')
const ActorDetails = require('./model/ActorCreditsDto')
const CastItemDto  = require('./model/CastItemDto')
var cache = require('./cache')
var response



/** 
 * init()
function init(){
    var details =[];
    response = null
    //cache =  new cache.MemoryCache()
    cache.MemoryCacheInit()
    //console.log('... Cache init() ...')
}
*/

module.exports = init

function init(dataSource) {
    response = null
    cache.MemoryCacheInit()
    const req = dataSource
        ? dataSource
        : require('request')
        
    return {
        getMoviesByName,
        getMoviesDetails,
        getPersonDetails,
        setResponse
    }


/*
module.exports = {
    getMoviesByName,
    getMoviesDetails,
    getPersonDetails,
    setResponse
}
*/

//1. https://api.themoviedb.org/3/search/movie?api_key=*****&name=war+games
function getMoviesByName(name, cb){
    const URI = `https://api.themoviedb.org/3/search/movie?api_key=${API_KEY}&query=${name}`;
 
    checkRequest(response, () =>{
        req(URI, (err, res, data) =>{   
        
            if(err) return cb(err)
             
            //const movie = JSON.parse(data.toString())
            const movie = new MovieSearch(JSON.parse(data.toString()))

            //console.log("\n\nEEE\n" + movie.toString())
            console.log(`URI: ${URI}`)
            cb(null, movie.results)
        })
    })
}


//2. https://api.themoviedb.org/3/movie/860?api_key=*****
function getMoviesDetails(movieId, cb){
    const URI = `https://api.themoviedb.org/3/movie/${movieId}?api_key=${API_KEY}`;

    checkRequest(response, () =>{
        if(cache.checkIfURIisInTheCache(URI)){
            console.log(`\nIs in the cache the URL: ${URI}`)
            const creditsAndDetails = cache.get(URI)
            //console.log(creditsAndDetails)
            cb(null, creditsAndDetails)
        }
        else{
            req(URI, (err, resp, data) =>{
                if(err) return cb(err)
                console.log(`URI: ${URI}`)
                //const credits = JSON.parse(data.toString())
                const creditsAndDetails = new MovieDetails(JSON.parse(data.toString()))
                creditsAndDetails.cast = []
                console.log(creditsAndDetails.toString())

                getMoviesCredits(movieId, (credits) =>{
                    creditsAndDetails.cast = credits.cast
                    creditsAndDetails.director = credits.director
                    //console.log("getMoviesDetails: " +  creditsAndDetails.original_title + ":::\ncredits.cast " + creditsAndDetails.cast[0].name + "\n\n");
                    cache.add(URI, creditsAndDetails)
                    cb(null, creditsAndDetails)
                })
            })
        }
        console.log("\n\ngetMoviesDetails DONE")
    })  


  
}


// 3. https://api.themoviedb.org/3/movie/860/credits?api_key=*****
function getMoviesCredits(movieId,cb){
    //console.log("\n\getMoviesCredits START")
    const URI = `https://api.themoviedb.org/3/movie/${movieId}/credits?api_key=${API_KEY}`;
    checkRequest(response, () =>{
        req(URI, (err, resp, data) =>{
            if(err) return cb(err)
            const movieCreditsJson = JSON.parse(data.toString())
            credits = new MovieCredits(movieCreditsJson)
            console.log(`URI: ${URI}`)
            cb(credits)
        })
    })
    console.log("getMoviesCredits END")
   // return details
}



// 4. https://api.themoviedb.org/3/person/4756/movie_credits?api_key=*****
function getPersonMovieDetails(actorId, cbPersonMovieCredits){
   // const URI = `https://api.themoviedb.org/3/movie/${actorId}/credits?api_key=${API_KEY}`;
    const URI = `https://api.themoviedb.org/3/person/${actorId}/movie_credits?api_key=${API_KEY}`
    checkRequest(response, () =>{
        req(URI, (err, resp, data) =>{
            if(err) return cb(err)
            const movieCredits = JSON.parse(data.toString())
            console.log(`URI: ${URI}`)
            cbPersonMovieCredits(movieCredits.cast)
        })
    })
    console.log("getPersonMovieDetails END")
}



// 5. https://api.themoviedb.org/3/person/8891?api_key=*****
function getPersonDetails(actorId, cb){
    //const URI = `https://api.themoviedb.org/3/movie/${movieId}/credits?api_key=${API_KEY}`;
    const URI = `https://api.themoviedb.org/3/person/${actorId}?api_key=${API_KEY}`
    checkRequest(response, () =>{
        if(cache.checkIfURIisInTheCache(URI)){
            console.log(`\nIs in the cache the URL: ${URI}`)
            const personAndMovieDetails = cache.get(URI)
            //console.log(personAndMovieDetails)
            cb(null, personAndMovieDetails)
        }
        else{
            req(URI, (err, resp, data) =>{
                if(err) return cb(err)  

                const movieCredits = JSON.parse(data.toString())
                var personAndMovieDetails = new ActorDetails(movieCredits)
                console.log(`URI: ${URI}`)           
                //personAndMovieDetails.cast = []
                getPersonMovieDetails(actorId, function(personMovCredits){
        /*
                    personMovCredits.forEach(function(element, idx) {
                        personAndMovieDetails.cast[idx] = new CastItemDto(element.id, element.name, element.character, element.title)
                    }, this);
        */
                    personAndMovieDetails.cast = personMovCredits
                    //console.log("\n\npersonAndMovieDetails.cast: "+ personAndMovieDetails.cast[0].title)
                    cache.add(URI, personAndMovieDetails)
                    cb(null, personAndMovieDetails)
                })
                //console.log(movieCredits.cast[0].toString())
            
            })
        }
    })
    console.log("getPersonDetails END")
}



function setResponse(resp){
    response = resp
}

}