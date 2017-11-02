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
	'serchMovies':{
		action: service.getMoviesByName,
		view: view('./views/moviesView.hbs')

	},
	 'movieById':{
		action: service.getMoviesByName,
		view: view('./views/movieDetailsView.hbs')

	},
	 'actorById':{
		action: service.getMoviesByName,
		view: view('./views/actorView.hbs')

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

/*	if(movies == urlObj.pathname && urlObj.query!= undefined){
		console.log("3. Lista de personagens de um filme")
	}*/
	if(actors  == urlObj.pathname || urlObj.pathname.match(actors)){
		 var movieId = urlObj.pathname.toString().split('/')
		 console.log("SSSSSSSSSSS" + movieId[2])
		 action = cb => service.getPersonMovieDetails(movieId[2], cb)
	}
/*
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
			    //actionCallBack()
				//const view = routes['serchMovies'].view
				//var html = view(obj.results)

				//const view = routes['movieById'].view
				//var html = view(obj)

				//const view = routes['actorById'].view
				//var html = view(obj)

				//console.log("\n\n\n\n" + obj.cast[0].toString())
				//data = html
				data = actionCallBack(urlObj, obj, data)
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

function actionCallBack(urlObj, obj,  data){		
	let view
	let html		
	if(movies == urlObj.pathname || urlObj.query.name != undefined){
		console.log('1. Pesquisa')        
		view = routes['serchMovies'].view
		html = view(obj)		
	}
	if(listOfmovie == urlObj.pathname || urlObj.pathname.match(listOfmovie)){ 
		console.log("2. Detalhes de um filme")
		 view = routes['movieById'].view
		 html = view(obj)
	}
/*	if(movies == urlObj.pathname && urlObj.query!= undefined){
		console.log("3. Lista de personagens de um filme")
	}*/
	if(actors  == urlObj.pathname || urlObj.pathname.match(actors)){
		 view = routes['actorById'].view
		 html = view(obj)
	}
	data = html
	return data
}


function view(viewPath) {
	const viewSrc = fs.readFileSync(viewPath).toString()
	return hbs.compile(viewSrc)
}