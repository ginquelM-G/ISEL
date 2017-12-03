const express = require('express')
const router = express.Router() 
const movieService = require('./movieService')()


module.exports = router


// ?? /search?name={query} ||  /movies?name={query}
router.get('/search', (req, resp, next) =>{
    movieService.getMoviesByName(req.query.name, (err, data) =>{
        if(err) return next(err)
        resp.render('moviesView', data)
       // resp.send('./views/moviesView.hbs', data)
    })
})




router.get('/movies/:id',  (req, resp, next) =>{
    movieService.getMoviesDetails(req.params.id, (err, data) =>{
        if(err) return next(err)
        resp.render('movieDetailsView', data)
        //resp.send('./views/movieDetailsView.hbs', data)
    })
})





router.use('/actors/:id',  (req, resp, next) =>{
    movieService.getPersonDetails(req.params.id, (err, data) =>{
        if(err) return next(err)
        resp.render('actorView', data)
        //resp.send('./views/actorView.hbs', data)
    })
})
