module.exports = Credits
const castItem = require('./CastItemDto')

function Credits(jsonData){
    this.cast = movieCredits.cast(jsonData.cast)
    this.director = movieCredits.director(jsonData.crew)

    console.log("Credits ctr")
     //return cast
}


var movieCredits = {
    cast: function(jsonCast){
        var cast = []
        jsonCast.forEach(function(element, indice, array) {
            cast[indice] = new castItem(element.id, element.name, element.character)
        }, this);
        return cast
    },
    director: function(jsonCrew){
         var directorName = ""
         jsonCrew.forEach(function(arrayCrew, idx, array){
            if(arrayCrew.job == "Director"){
                directorName = arrayCrew.name
                return arrayCrew.name
            }
         }, this);
         return directorName
    }

}