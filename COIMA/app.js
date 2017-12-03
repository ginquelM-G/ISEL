const http = require('http')
const url =  require('url')
const path = require('path')
const cookieParser = require('cookie-parser')
const passport = require('passport')
const session = require('express-session')
const bodyParser = require('body-parser')

const service = require('./routes/movieService')
const express = require('express')
const movieRouter = require('./routes/movieRoutes')
const userRouter = require('./routes/userRoutes')
const port = 3010


/*
	Setup express Web App
*/
const router = express() //	Init an empty pipeline of Middlewares
// view engine setup
router.set('views', path.join(__dirname, 'views'))
router.set('view engine', 'hbs')

// --------------------
router.use(bodyParser.urlencoded({ extended: false }))
router.use(cookieParser())
router.use(session({secret: 'keyboard cat', resave: false, saveUninitialized: true }))
router.use(passport.initialize())
router.use(passport.session()) // Obtem da sessão user id -> deserialize(id) -> user -> req.user

router.use(userRouter)
router.use(movieRouter)





// Init HTTP server
const server = http.createServer(router)
server.listen(port)

//Endpoints paths





movieRouter.use((err, req, resp, next) => {
    resp.statusCode = 500
    resp.setHeader('Content-Type', 'text/html')
    resp.end(err.message)    
})

movieRouter.use((req, resp) => {
    resp.statusCode = 404 // Resource Not Found
    resp.end()
})


