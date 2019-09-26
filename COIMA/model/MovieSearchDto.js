module.exports = MovieSearch

const MovieSearchItem = require('./MovieSearchItemDto')

function MovieSearch(json){
    const jsonInfo =json.results
    this.results  = results.MovieSearchItem(jsonInfo)
    //return this.results
}


var results = {
    MovieSearchItem: function(jsonInfo){
        //console.log(jsonInfo + "\n\nkkk")
        var itens = [];
        jsonInfo.forEach(function(element, indice, array) {
            itens[indice] = new MovieSearchItem(element.id, element.title)
            //console.log(itens[indice].__proto__)
        }, this);
        console.log("MovieSearch ctr \n")
        return itens
    }
}