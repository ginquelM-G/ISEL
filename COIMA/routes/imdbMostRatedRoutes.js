const userService = require('./userService')
const express = require('express')
const router = express.Router()
module.exports = router

const imdb_most_rated = [ 
    {
       
    }   
]

router.use((req, resp, next)=>{
    //console.log("==> "+imdb_most_rated)
    resp.locals.imdb_most_rated2 = imdb_most_rated
    next()
})

router.post('/imdb_most_rated2', (req, resp, next)=>{
    console.log('\n\n\n$$$$$$$$ imdb_most_rated \nreq.user: ')
    //console.log(req.user + '\n\n')
    req.user.imdb_most_rated.forEach(element => {
        console.log(element)
    });
    if(!req.user) return resp.redirect('/login')
    req.user.imdb_most_rated.push({
        id: req.body.id,
        title: req.body.title
    })

    console.log(req.body.id + " :: "+req.body.title)
    userService.save(req.user, (err) =>{
        if(err) return next(err)
        //resp.redirect(`/search?name=${req.query.name}`)
        resp.redirect('/imdb_most_rated')
    })
})



router.get('/imdb_most_rated',  (req, resp, next) =>{
    resp.render('imdbMostRated', {layout: false})
})
