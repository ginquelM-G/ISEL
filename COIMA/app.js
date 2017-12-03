const http = require('http')
const url =  require('url')
const path = require('path')
const service = require('./routes/movieService')
const express = require('express')
const movieRouter = require('./routes/movieRoutes')
const port = 3010


/*
	Setup express Web App
*/
const router = express() //	Init an empty pipeline of Middlewares
// view engine setup
router.set('views', path.join(__dirname, 'views'))
router.set('view engine', 'hbs')





// Init HTTP server
const server = http.createServer(router)
server.listen(port)

//Endpoints paths


/**

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
 */
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


