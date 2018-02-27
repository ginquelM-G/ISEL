package pt.isel.pdm.tmdb.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import pt.isel.pdm.tmdb.R
import pt.isel.pdm.tmdb.data.TheMovieDbClient
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.View
import pt.isel.pdm.tmdb.data.dtos.MovieDetails
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL


/**
 * Created by User01 on 25/10/2017.
 */
class ResultsOfMovieDetails: AppCompatActivity() {
    private val dbCLient = TheMovieDbClient()
    private var tv_movieDetails: TextView? = null
    private var img: ImageView? = null
    private var imgWidthS: Int?= null
    private val URL_IMG_HOST = "https://image.tmdb.org/t/p/original%s"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movies_details)

        tv_movieDetails = findViewById(R.id.tv_movieDetails) as TextView
        img = findViewById(R.id.imageView) as ImageView
        //imgWS = this.getResources().getDisplayMetrics().widthPixels /10;


        val movieDetails: Int = intent.getIntExtra("MOVIE_DETAILS",-1)

        if(-1 != movieDetails ){
            dbCLient.movieDetails(movieDetails, application, {movieItens ->
                //Toast.makeText(this, movieItens.toString(), Toast.LENGTH_SHORT).show()
                //Thread.sleep(4000)
                //tv_movieDetails?.text = movieItens.toString()
                displayImgAndMovieDetails(movieItens, img, movieItens.poster_path)
            })
        }

    }

    private fun displayImgAndMovieDetails(movieItens: MovieDetails, imageView: ImageView?, url: String) {
        //Toast.makeText(this, String.format(USL_HOST, url), Toast.LENGTH_SHORT).show()
        var b: Bitmap? =null

        object : AsyncTask<Void, Int, String>() {
            override fun doInBackground(vararg params: Void?): String {
                try {
                    var url_ =  URL(String.format(URL_IMG_HOST, url))
                    var inputStream  =  BufferedInputStream(url_.openStream())
                    b = BitmapFactory.decodeStream(inputStream)
                } catch(e: Exception){
                    System.err.println("\n${e.message}")
                }
                System.out.println("\n\n${String.format(URL_IMG_HOST, url)}")
                return "DONE"
            }
            override fun onProgressUpdate(vararg values: Int?) {
                System.out.println("\n\nonProgressUpdate")
                //imageView?.setImageBitmap(b)
            }
            override fun onPostExecute(result: String?) {
                System.out.println("\n\nonPostExecute")
                imageView?.setImageBitmap(b)
                tv_movieDetails?.text = movieItens.toString()
            }
        }.execute()
    }


}