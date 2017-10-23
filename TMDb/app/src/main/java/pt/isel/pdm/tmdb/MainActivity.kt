package pt.isel.pdm.tmdb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import pt.isel.pdm.tmdb.db.TheMovieDbClient
import pt.isel.pdm.tmdb.db.model.MovieSearchItem


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var searchMovieItems = dbCLient.search("Batman", application)
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchMovieItems)
        listOfItem.adapter = adapter

        listOfItem.setOnItemClickListener { parent, view, position, ld ->
            Toast.makeText(this, searchMovieItems.toString(), Toast.LENGTH_SHORT).show()
        }

        /**
        application.requestQueue.add(JsonObjectRequest(
        //requestQueue.add(JsonObjectRequest(
                MOVIE_SEARCH,
                null,
                {
                    val jsonSearchMovieItem = it.get("results") as JSONArray


                    Log.i("jsonSearchMovieItem:", jsonSearchMovieItem.toString())


                    val searchMovieItems = jsonSearchMovieItem
                            .asSequence()
                            .map {
                                MovieSearchItem(
                                        it["id"] as Int,
                                        it["title"] as String
                                )
                            }
                            .toList()
                            .toTypedArray()


                    //Log.d("########MovieSearchItem_", MovieSearchItem_..toString())

                    var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchMovieItems)
                    listOfItem.adapter = adapter

                    listOfItem.setOnItemClickListener { parent, view, position, ld ->
                        Toast.makeText(this, searchMovieItems[position].toString(), Toast.LENGTH_SHORT).show()
                    }

                },

                {
                    Log.e("ERROR:: ", it.toString())

                })

        )
        */
    }



    fun JSONArray.asSequence() =
            (0 until length()).asSequence().map { get(it) as JSONObject }
}
