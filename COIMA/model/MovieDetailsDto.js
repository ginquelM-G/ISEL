module.exports = MovieDetails

const cred = require('./CreditsDto')

//MovieDetails.prototype = new cred()
//MovieDetails.prototype.constructor = cred

/** Precisa ser feito agregação ou composição */

//function MovieDetails(id, originalTitle, overview, poster_path){
function MovieDetails(jsonData){
    this.id = jsonData.id
    this.original_title = jsonData.original_title
    this.overview = jsonData.overview
    this.poster_path = jsonData.poster_path
    //this.cast = [];

    console.log("MOVIE_DEYAILS\n "+ this.id+ "\n\n"+ this.original_title)

    //console.log("HELL "+castS[0].name.toString())

 /*  castS.forEach(function(element, indice, array) {
        this.cast[indice] = castS
    }, this);
    */

}