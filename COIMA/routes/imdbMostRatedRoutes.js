const userService = require('../services/userService')
const express = require('express')
const router = express.Router()
module.exports = router


router.use((req, resp, next)=>{
    //console.log("==> "+imdb_most_rated)
    //resp.locals.imdb_most_rated2 = imdb_most_rated
    next()
})


function addOrDel(req, resp, next){
    if(!req.user) return resp.redirect('/login')
   /*  req.user.imdb_most_rated.forEach(element => {
        console.log(element)
    }); */
    userService.save(req.user, (err) =>{
        if(err) return next(err)
        //resp.redirect(`/search?name=${req.query.name}`)
        resp.redirect('/imdb_most_rated')
    })
}


router.post('/add_imdb_most_rated', (req, resp, next) => {
    if(!req.user) return resp.redirect('/login')
    req.user.imdb_most_rated.push({
        id: req.body.id,
        title: req.body.title
    })
    addOrDel(req, resp, next)
})

router.post('/del_imdb_most_rated', (req, resp, next) => {
    if(!req.user) return resp.redirect('/login')
    req.user.imdb_most_rated.forEach((element, idx, imdb_most_rated) => {
        if(element.id == req.body.id){
            req.user.imdb_most_rated.splice(idx, 1)
        }
    });
    addOrDel(req, resp, next)
})



router.get('/imdb_most_rated',  (req, resp, next) =>{
    resp.render('imdbMostRated', {layout: false})
})
