module.exports = CastItemDto

function CastItemDto(id, name, character, title){
    this.id= id
    this.name =name
    this.character = character
    this.title = title //vai ser usado em filmes que o actor participou
}