const express = require('express')
const router = express.Router()
const userService = require('../services/userService')
const passport = require('passport')


module.exports = router


router.get('/sign_up', function(req, res, next) {
  res.render('signUp', {layout: false})
})

router.get('/login', function(req, res, next) {
  const ctx = { layout: false }
  const msg = req.flash('loginError')
  if(msg)  ctx.loginError = {message: msg}
  res.render('login', ctx)
})


router.post('/login', (req, resp, next)=>{
  //console.log(req.body)
 // console.log('Beggin USERNAME: ' + req.body.username + ' == ' + req.params.username + " req.user: " + req.username )
  userService.authenticate(req.body.username, req.body.password, (err, user, info)=>{
    if(err) return next(err)
    //if(info) return next(new Error(info))
    if(info) {
      // gravar mensagem de erro
      req.flash('loginError', info)
      return resp.redirect('/login')
    }
    req.logIn(user, (err)=>{
      if(err) return next(err)
      console.log('USERNAME: ' + req.body.username )
      //resp.redirect('/')
      resp.redirect('/account/'+user.username)
    })
  })
})

router.post('/sign_up', (req, resp, next)=>{


  userService.createACommentFile(req.body, (err) =>{
    if(err) return next(err)
    console.log('\n\nCreated a Comment File In CouchDB if doesn\'t exists.')
    //resp.redirect('/login')
  })

  userService.createNewFileInCouchDB(req.body, (err) =>{
      if(err) return next(err)
      console.log('\n\nCreated a New File In CouchDB.')
      resp.redirect('/login')
  })

  
  /*
  userService.authenticate(req.body.username, req.body.password, (err, user, info)=>{
    if(err) return next(err)
    if(info) return next(new Error(info))
    req.logIn(user, (err)=>{
      if(err) return next(err)
      resp.redirect('/login')
      //resp.redirect('/account/'+user.username)
    })
  })
  */
})



router.use((req, res, next)=>{
  if(req.user){
     res.locals.classics = req.user.classics
     res.locals.best_all_time = req.user.best_all_time
     res.locals.imdb_most_rated = req.user.imdb_most_rated
     res.locals.to_see = req.user.to_see
     res.locals.seen = req.user.seen
  }
  else{
     res.locals.classics = []
     res.locals.best_all_time = []
     res.locals.imdb_most_rated = []
     res.locals.to_see = []
     res.locals.seen = []
    }
  next()
})

passport.serializeUser(function(user, cb){
  cb(null, user.username)
})

passport.deserializeUser(function(username, cb){
  userService.find(username, cb)
})