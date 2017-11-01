package pt.isel.pdm.tmdb.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import pt.isel.pdm.tmdb.R
import pt.isel.pdm.tmdb.data.TheMovieDbClient
import pt.isel.pdm.tmdb.data.dtos.MovieSearchItem

/**
 * Created by User01 on 25/10/2017.
 */
class ResultsActivity: AppCompatActivity() {

    private val listOfItem by lazy { findViewById(R.id.list_itens) as ListView }
    private val dbCLient = TheMovieDbClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_results)

        val movieName: String = intent?.getStringExtra("MOVIE_NAME")?:"empty"
        val movieNP: String = intent?.getStringExtra("MOVIE_NOW_PLAYING")?:"empty"
        val movieUpC: String = intent?.getStringExtra("MOVIE_UPCOMING")?:"empty"
        val moviePop: String = intent?.getStringExtra("MOVIE_POPULAR")?:"empty"
        //val movieDetails: Int = intent.getIntExtra("MOVIE_DETAILS",-1)?:-1

        if("movieNowPlaying".equals(movieNP)){
            dbCLient.movieNowPlaying(application, {movieItens -> callBack(movieItens)})
        }
        else  if("movieUpcoming".equals(movieUpC)){
            dbCLient.movieUpcoming(application, {movieItens -> callBack(movieItens)})

        }else if("moviePopular".equals(moviePop)){
            dbCLient.moviePopular(application, {movieItens -> callBackTOPopularMovies(movieItens)})
        }
        else if(!"empty".equals(movieName) || !"".equals(movieName)){
            dbCLient.search(movieName, application, {movieItens -> callBack(movieItens)})
        }//else if(-1 != movieDetails ){ //dbCLient.movieDetails(movieDetails, application, {movieItens -> callBack(movieItens)}) }

    }


    fun callBack(movieItems: Array<MovieSearchItem>){
            var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieItems)
            listOfItem.adapter = adapter
            listOfItem.setOnItemClickListener { parent, view, position, ld ->
                Toast.makeText(this, movieItems.toString(), Toast.LENGTH_SHORT).show()
            }
    }


    fun callBackTOPopularMovies(movieItems: Array<MovieSearchItem>){
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieItems)
        listOfItem.adapter = adapter
        listOfItem.setOnItemClickListener { parent, view, position, ld ->
            Toast.makeText(this, movieItems[position].toString(), Toast.LENGTH_SHORT).show()
            var intent = Intent(this, ResultsOfMovieDetails::class.java)
            intent.putExtra("MOVIE_DETAILS", movieItems[position].id)

            startActivity(intent)
            //edTxt_idDoFilme?.setText("")
        }
    }

}