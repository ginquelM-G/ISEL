const express = require('express')
const router = express() // Init empty pipeline
const movieService = require('./movieService')()


module.exports = router


// ?? /search?name={query} ||  /movies?name={query}
router.use('/search', (req, resp, next) =>{
    movieService.getMoviesByName(req.query.name, (err, data) =>{
        if(err) return next(err)
        resp.send('./views/moviesView.hbs', data)
    })
})




router.use('/movies/:id',  (req, resp, next) =>{
    movieService.getMoviesDetails(req.params.id, (err, data) =>{
        if(err) return next(err)
        resp.send('./views/movieDetailsView.hbs', data)
    })
})





router.use('/actors/:id',  (req, resp, next) =>{
    movieService.getPersonDetails(req.params.id, (err, data) =>{
        if(err) return next(err)
        resp.send('./views/actorView.hbs', data)
    })
})
