const http = require('http')
const url =  require('url')
const service = require('./movieService')
const fs = require('fs')
const hbs = require('handlebars')
const port = 3010


// Init HTTP server
const server = http.createServer(router)
server.listen(port)

//Endpoints paths
const movies = '/movies' // query-string ?name=.. 
const listOfmovie = '/movies\/[0-9]+'
const listOfPerson = '/movies\/[0-9]+/'
const actors = '/actors\/[0-9]+' 

const routes = {
    'movies':{
        action: service.getMoviesByName,
        view: view('./views/moviesView.hbs')

    }

}

function router(req, resp){
    const urlObj = url.parse(req.url, true)
    let action
    //console.log('=> '+ urlObj.pathname)
    //console.log('==> '+ urlObj.query.name)
   

    if(movies == urlObj.pathname || urlObj.query.name != undefined){
        console.log('1. Pesquisa')        
        action = cb => service.getMoviesByName(urlObj.query.name, cb)
    }
    if(listOfmovie == urlObj.pathname || urlObj.pathname.match(listOfmovie)){ 
        console.log("2. Detalhes de um filme")
        var movieId = urlObj.pathname.split('/')
        //console.log(movieId[2])
        action = cb => service.getMoviesDetails(movieId[2], cb)
    }
/*
    if(movies == urlObj.pathname && urlObj.query!= undefined){
        console.log("3. Lista de personagens de um filme")
    }
    if(actors  == urlObj.pathname || urlObj.pathname.match(actors)){
         console.log("YESSSSSSS")
    }
    if(actors  == urlObj.pathname || urlObj.pathname.match(actors)){
         console.log("Detalhes de uma pessoa")
    }
    */
    if(action != undefined){ 
        const obj = action((err, obj) =>{
            let data 
            if(err){
                data = err.message
                resp.statusCode = 500
                console.log("ERROR: "+err.message)
                //throw err
            }else{
                //data = html(obj)
                //data = JSON.stringify(obj)
                const vieww = routes['movies'].view
                var html = vieww(obj.results)
                console.log("\n\n\n\n" + obj.toString)
                data = html
                //data = view(obj)
                resp.statusCode = 200
            }
            resp.setHeader('Content-Type', 'text/html')
            resp.end(data)
        })
    }
    else{
    //(resp != 200){
        console.log(resp.toString())
        resp.statusCode = 404 // Resource Not Found
        resp.end()
    }
    //resp.end()

}


/*
function html(obj){
    let str = ''
    for(let prop in obj){
        const val = obj[prop]
        str += `<li>${prop}:  ${val}</li>`
    }
    return `<ul>${str}</ul>`
}
*/


function view(viewPath) {
    const viewSrc = fs.readFileSync(viewPath).toString()
    return hbs.compile(viewSrc)
}