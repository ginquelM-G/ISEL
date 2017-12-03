const userService = require('./userService')
const express = require('express')
const router = express.Router()
module.exports = router

const classics = [
   
    {
        "id": "10134",
        "name": "Cyborg"
    },
     {
        "id": "272",
        "title": "Batman Begins123"
    }
]

router.use((req, resp, next)=>{
    resp.locals.classics2 = classics
    next()
})

router.post('/classics2', (req, resp, next)=>{
    console.log('\n\n\n$$$$$$$$ classicsRoutes \nreq.user: ')
    //console.log(req.user + '\n\n')
    req.user.classics.forEach(element => {
        console.log(element)
    });
    if(!req.user) return resp.redirect('/login')
    req.user.classics.push({
        id: req.body.id,
        title: req.body.title
    })
    userService.save(req.user, (err) =>{
        if(err) return next(err)
        //resp.redirect(`/search?name=${req.query.name}`)
        resp.redirect('/classics')
    })
})



router.get('/classics',  (req, resp, next) =>{
    resp.render('classics', {layout: false})
})
