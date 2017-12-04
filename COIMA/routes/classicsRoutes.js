const userService = require('./userService')
const express = require('express')
const router = express.Router()
module.exports = router

router.use((req, resp, next)=>{
    //resp.locals.classics = classics
    next()
})


function addOrDel(req, resp, next){
    if(!req.user) return resp.redirect('/login')
    req.user.classics.forEach(element => {
        console.log(element)
    });
    userService.save(req.user, (err) =>{
        if(err) return next(err)
        //resp.redirect(`/search?name=${req.query.name}`)
        resp.redirect('/classics')
    })
}


router.post('/add', (req, resp, next) => {
    if(!req.user) return resp.redirect('/login')
    req.user.classics.push({
        id: req.body.id,
        title: req.body.title
    })
    addOrDel(req, resp, next)
})

router.post('/del', (req, resp, next) => {
    if(!req.user) return resp.redirect('/login')
    req.user.classics.forEach((element, idx, classics) => {
        if(element.id == req.body.id){
            req.user.classics.splice(idx, 1)
        }
    });
    addOrDel(req, resp, next)
})


router.get('/classics',  (req, resp, next) =>{
    resp.render('classics', {layout: false})
})
