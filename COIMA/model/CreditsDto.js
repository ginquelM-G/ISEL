module.exports = Credits
const castItem = require('./CastItemDto')

function Credits(jsonData){
    var jsonInfo = jsonData.cast
    var cast = [];
   
    jsonInfo.forEach(function(element, indice, array) {
        cast[indice] = new castItem(element.id, element.name, element.character)
    }, this);

    return cast
}