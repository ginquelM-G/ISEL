const userService = require('./userService')
const express = require('express')
const router = express.Router()
module.exports = router


router.use((req, resp, next)=>{
    //resp.locals.best_all_time2 = best_all_time
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
        resp.redirect('/best_all_time')
    })
}


router.post('/add_to_best_all_time', (req, resp, next) => {
    if(!req.user) return resp.redirect('/login')
    req.user.best_all_time.push({
        id: req.body.id,
        title: req.body.title
    })
    addOrDel(req, resp, next)
})

router.post('/del_to_best_all_time', (req, resp, next) => {
    if(!req.user) return resp.redirect('/login')
    req.user.best_all_time.forEach((element, idx, best_all_time) => {
        if(element.id == req.body.id){
            req.user.best_all_time.splice(idx, 1)
        }
    });
    addOrDel(req, resp, next)
})



router.get('/best_all_time',  (req, resp, next) =>{
    resp.render('bestAllTime', {layout: false})
})
