package pt.isel.pdm.tmdb.data

import android.app.Application
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import pt.isel.pdm.tmdb.data.dtos.MovieDetails
import pt.isel.pdm.tmdb.data.dtos.MovieSearchItem
import pt.isel.pdm.tmdb.requestQueue
import java.util.*

/**
 * Created by User01 on 22/10/2017.
 */
class TheMovieDbClient {

    private val API_KEY : String = "b531994cdfaa8e3b441e4086b1c6756d"
    private val SEARCH_QUERY: String ="https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s&language=%s"
    private val MOVIE_NOW_PLAYING_QUERY: String ="https://api.themoviedb.org/3/movie/now_playing?api_key=%s&language=%s"
    private val MOVIE_UPCOMING_QUERY: String ="https://api.themoviedb.org/3/movie/upcoming?api_key=%s&language=%s"
    private val MOVIE_POPULAR_QUERY: String ="https://api.themoviedb.org/3/movie/popular?api_key=%s&language=%s"
    private val MOVIE_BY_ID_QUERY: String ="https://api.themoviedb.org/3/movie/%d?api_key=%s&language=%s"


    /*
    	considere	os	seguintes	endpoints:
            • Pesquisa	de	filmes	por	nome	-	https://developers.themoviedb.org/3/search/search-movies
            • Filmes	em	cartaz	                -	hPps://developers.themoviedb.org/3/movies/get-now-playing
            • Estreias	para	breve	        -	hPps://developers.themoviedb.org/3/movies/get-upcoming
            • Filmes	mais	populares	        -	hPps://developers.themoviedb.org/3/movies/get-popular-movies
            • Informação	do	filme	            -	hPps://developers.themoviedb.org/3/movies/get-movie-details
     */



    private var lang = Locale.getDefault().language
    private fun JSONArray.asSequence() = (0 until length()).asSequence().map { get(it) as JSONObject }


    /// Pesquisa de	filmes por nome
    /// e.g.: https://api.themoviedb.org/3/search/movie?api_key=*****&query=war%20games
    fun search(title: String?, application: Application, arrayCb: (Array<MovieSearchItem>) -> Unit) : Unit{
        val query: String = String.format(SEARCH_QUERY, API_KEY, title, getLanguage(lang))
        println(query)

        //addRequestQueue(query, application, arrayCb)

        application.requestQueue.add(JsonObjectRequest(
                //requestQueue.add(JsonObjectRequest(
                query,
                null,
                {
                    val jsonSearchMovieItem = it.get("results") as JSONArray
                    Log.i("jsonSearchMovieItem:", jsonSearchMovieItem.toString())

                    val searchMovieItems = jsonSearchMovieItem
                            .asSequence()
                            .map {
                                MovieSearchItem(
                                        it["id"] as Int,
                                        it["title"] as String,
                                        null
                                )
                            }
                            .toList()
                            //.toString()
                            .toTypedArray()

                    arrayCb(searchMovieItems)
                    //Log.d("########MovieSearchItem_", MovieSearchItem_..toString())
                },
                {
                    Log.e("ERROR:: ", it.toString())

                })
        )

    }


    /// Filmes	em	cartaz
    /// e.g.: https://api.themoviedb.org/3/movie/now_playing?api_key=<<api_key>>&language=en-US&page=1
    fun movieNowPlaying(application: Application, resultCb: (Array<MovieSearchItem>) -> Unit){
        val query: String = String.format(MOVIE_NOW_PLAYING_QUERY, API_KEY, getLanguage(lang))
        println(query)
        addRequestQueue(query, application, resultCb)
    }





    /// Estreias para breve
    /// e.g.: https://api.themoviedb.org/3/movie/upcoming?api_key=<<api_key>>&language=en-US&page=1
    fun movieUpcoming(application: Application, resultCb: (Array<MovieSearchItem>) -> Unit){
        val query: String = java.lang.String.format(MOVIE_UPCOMING_QUERY, API_KEY, getLanguage(lang))
        println(query)
        addRequestQueue(query, application, resultCb)
    }


    /// Pesquisa de	filmes por nome
    /// e.g.: https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
    fun moviePopular(application: Application, resultCb: (Array<MovieSearchItem>) -> Unit){
        val query: String = java.lang.String.format(MOVIE_POPULAR_QUERY, API_KEY, getLanguage(lang))
        println(query)
        addRequestQueue(query, application, resultCb)
    }



    /// Informação do filme
    /// e.g.: https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>
    //public fun movieDetails(id: Int, application: Application, resultCb: (Array<MovieSearchItem>) -> Unit){
    fun movieDetails(id: Int, application: Application, resultCb: (String) -> Unit){
        //if (lang.equals("pt"))lang="pt-PT" else lang="en-EU"
        val query: String = String.format(MOVIE_BY_ID_QUERY, id, API_KEY,getLanguage(lang))
        println(query)
        //addRequestQueue(query, application, arrayCb)

        application.requestQueue.add(JsonObjectRequest(
                query,
                null,
                {
                val movieDetails =
                        MovieDetails(
                                it["id"] as Int,
                                it["title"] as String,
                                it["popularity"] as Double,
                                it["vote_average"] as Double,
                                it["release_date"] as String,
                                it["overview"] as String
                                ).toString()

                    System.err.println("\n\nmovieDetails:\n"+ movieDetails)
                    resultCb(movieDetails)
                },
                {
                    Log.e("ERROR:: ", it.toString())

                })
        )
    }


    private fun addRequestQueue(query: String, application: Application, arrayResultCb: (Array<MovieSearchItem>) -> Unit){
        application.requestQueue.add(JsonObjectRequest(
                query,
                null,
                {
                    val jsonSearchMovieItem = it.get("results") as JSONArray
                    Log.i("jsonSearchMovieItem:", jsonSearchMovieItem.toString())

                    val searchMovieItems = jsonSearchMovieItem
                            .asSequence()
                            .map {
                                MovieSearchItem(
                                        it["id"] as Int,
                                        it["title"] as String,
                                        it["release_date"] as String
                                )
                            }
                            .toList()
                            //.toString()
                            .toTypedArray()

                    arrayResultCb(searchMovieItems)
                },
                {
                    Log.e("ERROR:: ", it.toString())

                })
        )
    }




    private fun getLanguage(lang: String): String{
        when(lang) {
            "pt" -> return "pt-PT"
            "en" -> return "en-US"
        }
        return "en-US"
    }
}