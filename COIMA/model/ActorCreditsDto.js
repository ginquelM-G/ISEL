module.exports = ActorCredits


function ActorCredits(jsonData){
    this.birthday = jsonData.birthday
    this.name = jsonData.name
    this.biography = jsonData.biography
    this.cast = []
}