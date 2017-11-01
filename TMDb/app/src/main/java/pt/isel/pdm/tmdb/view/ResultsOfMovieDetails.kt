package pt.isel.pdm.tmdb.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import pt.isel.pdm.tmdb.R
import pt.isel.pdm.tmdb.data.TheMovieDbClient

/**
 * Created by User01 on 25/10/2017.
 */
class ResultsOfMovieDetails: AppCompatActivity() {
    private val dbCLient = TheMovieDbClient()
    private var tv_movieDetails: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movies_details)

        tv_movieDetails = findViewById(R.id.tv_movieDetails) as TextView

        val movieDetails: Int = intent.getIntExtra("MOVIE_DETAILS",-1)

        if(-1 != movieDetails ){
            dbCLient.movieDetails(movieDetails, application, {movieItens ->
                //Toast.makeText(this, movieItens.toString(), Toast.LENGTH_SHORT).show()
                tv_movieDetails?.text = movieItens
            })
        }

    }


}