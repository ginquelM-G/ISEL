const http = require('http')
const url =  require('url')
const path = require('path')
const hbs = require('hbs')
const cookieParser = require('cookie-parser')
const passport = require('passport')
const session = require('express-session')
const bodyParser = require('body-parser')
const flashM = require('connect-flash')

const service = require('./services/movieService')
const express = require('express')
const movieRouter = require('./routes/movieRoutes')
const userRouter = require('./routes/userRoutes')
const classicsRouter = require('./routes/classicsRoutes')
const bestAllTimeRouter = require('./routes/bestAllTimeRoute')
const imdbMostRated = require('./routes/imdbMostRatedRoutes')
const port = 3010


/*
	Setup express Web App
*/
const router = express() //	Init an empty pipeline of Middlewares
// view engine setup
router.set('views', path.join(__dirname, 'views'))
router.set('view engine', 'hbs')
hbs.registerPartials(__dirname + '/views/partials')


// --------------------
router.use(bodyParser.urlencoded({ extended: false }))
router.use(express.static(path.join(__dirname, 'public'))) // ter acesso aos ficheiros do directorio public(ex: .css)
router.use(express.static('views')) 
router.use(express.static('routes'))
//router.use('/static', express.static(path.join(__dirname, '/routes')))
router.use(cookieParser())
router.use(session({secret: 'keyboard cat', resave: false, saveUninitialized: true }))
/*
* are invoked on each request and 
* they are the ones that call serializeUser  and deserializer
*/
router.use(passport.initialize()) // is invoked on each request and is the one that call serializeUser to load the user id to req.user
router.use(passport.session()) // Obtem da sessão user id -> deserialize(id) -> user -> req.user
router.use(flashM()) // Flash Message

router.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', "*");
    res.header('Access-Control-Allow-Methods','GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    next();
})

router.use(userRouter)
router.use(classicsRouter)
router.use(bestAllTimeRouter)
router.use(imdbMostRated)
router.use(movieRouter)





// Init HTTP server
const server = http.createServer(router)
server.listen(port)

//Endpoints paths





movieRouter.use((err, req, resp, next) => {
    resp.statusCode = 500
    //resp.setHeader('Content-Type', 'text/html')
    resp.end(err.message)    
})

movieRouter.use((req, resp) => {
    resp.statusCode = 404 // Resource Not Found
    resp.end()
})


