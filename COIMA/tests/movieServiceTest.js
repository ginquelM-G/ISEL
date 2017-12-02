

const movieS = require('./../movieService')

module.exports = {
    testGetMoviesByName,
    testGetMoviesDetails,
    testGetPersonDetails
}





function testGetMoviesByName(test){

    movieS.getMoviesByName('Batman', (err, movieResults) =>{
        if(err) 
            test.ifError(err)
        else {
            //console.log(movieResults)      
            test.equal(movieResults[0].id, 268)
            test.equal(movieResults[0].title, 'Batman')
            test.equal(movieResults[2].id, 272)
            test.equal(movieResults[2].title, 'Batman Begins')
            //tests.equal(movieResults[0].overview, "The Dark Knight of Gotham City begins his war on crime with his first major enemy being the clownishly homicidal Joker, who has seized control of Gotham's underworld.")
        }
        test.done()
    })
}


function testGetMoviesDetails(test){
    movieS.getMoviesDetails(268, (err, creditsAndDetails)=>{    
        if(err) test.ifError(err)
        else{
            test.equal(creditsAndDetails.director, 'Tim Burton')
            test.equal(creditsAndDetails.cast[0].name, 'Jack Nicholson')
        }
        test.done()
    })
}


function  testGetPersonDetails(test){
    movieS.getPersonDetails(514, (err, personAndMovieDetails) =>{
        if(err) test.ifError(err)
        else{
            test.equal(personAndMovieDetails.name, 'Jack Nicholson')          
            test.equal(personAndMovieDetails.birthday, '1937-04-22')
            test.equal(personAndMovieDetails.cast[2].title, 'Easy Rider')
        }
        test.done()
    })
}