package pt.isel.pdm.tmdb.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import pt.isel.pdm.tmdb.R
import pt.isel.pdm.tmdb.data.TheMovieDbClient


class MainActivity : AppCompatActivity() {

    /**

    data class MovieSearchItem_(val id: Int, val title: String) {
        override fun toString(): String = "Id: " + id + "\nTitle: " + title

    }
*/

    private val requestQueue by lazy { Volley.newRequestQueue(this) }
    private val listOfItem by lazy { findViewById(R.id.list_itens) as ListView}
    private val MOVIE_SEARCH = "https://api.themoviedb.org/3/search/movie?api_key=b531994cdfaa8e3b441e4086b1c6756d&query=Batman"
    private val dbCLient = TheMovieDbClient()
    private var movieName1: String? = null
    private var edTxt_nomeDoFilme: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_1)

        edTxt_nomeDoFilme = findViewById(R.id.nomeDoFilme) as EditText
        var bt_searchMovieByName = findViewById(R.id.bt_searchMovieByName) as Button
        var bt_movieNowPlaying = findViewById(R.id.bt_movieNowPlaying) as Button
        var bt_movieUpcoming = findViewById(R.id.bt_movieUpcoming) as Button
        var bt_moviePopular = findViewById(R.id.bt_moviePopular) as Button


      /*
        bt_searchMovieByName.setOnClickListener {startActivity(Intent(this, ResultsActivity::class.java))  }
      */
        bt_searchMovieByName.setOnClickListener(getOnClickListener())
        bt_movieNowPlaying.setOnClickListener(getOnClickListener())




    }


    private fun getOnClickListener(): View.OnClickListener{
        return View.OnClickListener {
            var intent =  Intent(this, ResultsActivity::class.java)
            intent.putExtra("MOVIE_NAME",  edTxt_nomeDoFilme?.text.toString())

            startActivity(intent)
            //startActivity(Intent(this, ResultsActivity::class.java))
            edTxt_nomeDoFilme?.setText("")

        }
    }

/**
    private fun getOnClickListener2():  View.OnClickListener{

        return View.OnClickListener {
            dbCLient.movieNowPlaying(application, { movieItems ->
                var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieItems)
                listOfItem.adapter = adapter
                listOfItem.setOnItemClickListener { parent, view, position, ld ->
                    Toast.makeText(this, movieItems.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    */



    fun JSONArray.asSequence() =
            (0 until length()).asSequence().map { get(it) as JSONObject }
}
