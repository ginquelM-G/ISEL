module.exports = MovieDetails

const cred = require('./CreditsDto')

//MovieDetails.prototype = new cred()
//MovieDetails.prototype.constructor = cred

/** Precisa ser feito agregação ou composição */

function MovieDetails(id, originalTitle, overview, poster_path, castS){
console.log("MOOVEI_DEYAILS")
    this.id = id
    this.original_title =originalTitle
    this.overview = overview
    this.poster_path = poster_path
    this.cast = [];

    console.log("HELL "+castS[0].name.toString())

    castS.forEach(function(element, indice, array) {
        this.cast[indice] = castS
    }, this);

}