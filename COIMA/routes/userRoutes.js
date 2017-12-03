const express = require('express')
const router = express.Router()
const userService = require('./userService')
const passport = require('passport')

module.exports = router



router.get('/login', function(req, res, next) {
  console.log('LOGIN ON')
  res.render('login', {layout: false});
  console.log('after')
});


router.post('/login', (req, resp, next)=>{
  console.log('USER $$$$$$$$$$$$$')
  console.log(req.body)


  userService.authenticate(req.body.username, req.body.password, (err, user, info)=>{
    if(err) return next(err)
    if(info) return next(new Error(info))
    req.logIn(user, (err)=>{
      if(err) return next(err)
      resp.redirect('/')
    })
  })
})


router.use((req, res, next)=>{
  if(req.user) res.locals.favourites = req.user.id
  else res.locals.favourites=[]
  next()
})

passport.serializeUser(function(user, cb){
  cb(null, user.username)
})

passport.deserializeUser(function(username, cb){
  userService.find(username, cb)
})