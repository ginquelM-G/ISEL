const userService = require('./userService')
const express = require('express')
const router = express.Router()
module.exports = router

const best_all_time = [
   
    {
       
    }   
]

router.use((req, resp, next)=>{
    resp.locals.best_all_time2 = best_all_time
    console.log("___USE__ resp.locals.best_all_time: " + resp.locals.best_all_time2 )
    next()
})

router.post('/best_all_time2', (req, resp, next)=>{
    console.log('\n\n\n$$$$$$$$ best_all_time \nreq.user: ')
    //console.log(req.user + '\n\n')
    req.user.best_all_time.forEach(element => {
        console.log(element)
    });
    if(!req.user) return resp.redirect('/login')
    req.user.best_all_time.push({
        id: req.body.id,
        title: req.body.title
    })
    userService.save(req.user, (err) =>{
        if(err) return next(err)
        //resp.redirect(`/search?name=${req.query.name}`)
        resp.redirect('/best_all_time')
    })
})



router.get('/best_all_time',  (req, resp, next) =>{
    resp.render('bestAllTime', {layout: false})
})
