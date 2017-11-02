module.exports = MovieSearch

const MovieSearchItem = require('./MovieSearchItemDto')

function MovieSearch(json){
    const jsonInfo =json.results
    var itens = [];
    jsonInfo.forEach(function(element, indice, array) {
        itens[indice] = new MovieSearchItem(element.id, element.title)
    }, this);
    //console.log("MovieSearch ctr")
    return itens
}