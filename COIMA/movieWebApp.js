const http = require('http')
const url =  require('url')
const service = require('./movieService')
const fs = require('fs')
const hbs = require('handlebars')
const express = require('express')
const movieRouter = require('./movieRoutes')
const port = 3010


// Init HTTP server
const router = express() //	Init an empty pipeline of Middlewares
const server = http.createServer(router)
server.listen(port)

//Endpoints paths

router.use((req, resp, next) => {
	const urlObj = url.parse(req.url, true)
	//service.setResponse(resp)
	//console.log('=> '+ urlObj.pathname)
	//console.log('==> '+ urlObj.query.name)
	
	req.query = urlObj.query
	resp.send = function(viewPath, ctx){
		const html = view(viewPath)(ctx)
		resp.setHeader('Content-Type', 'text/html')
		resp.end(html)
	}
	next()
})

router.use(movieRouter)


movieRouter.use((err, req, resp, next) => {
    resp.statusCode = 500
    resp.setHeader('Content-Type', 'text/html')
    resp.end(err.message)    
})

movieRouter.use((req, resp) => {
    resp.statusCode = 404 // Resource Not Found
    resp.end()
})



/**
 * Returns template Handlebars.
 * 
 * @param {*} viewPath Path for handlebars template source.
 */
function view(viewPath) {
	if(!view.paths) view.paths = {} // Init paths cache
    if(view.paths[viewPath]) return view.paths[viewPath]
    const viewSrc = fs.readFileSync(viewPath).toString()
    const template = hbs.compile(viewSrc)
    view.paths[viewPath] = template
    return template
}
