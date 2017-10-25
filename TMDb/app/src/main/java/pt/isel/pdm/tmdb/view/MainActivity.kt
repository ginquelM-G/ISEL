package pt.isel.pdm.tmdb.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
        val textBox = findViewById(R.id.textBox) as TextView


        var searchMovieItems = dbCLient.search("Batman", application, {x ->
            //var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,x)
           // listOfItem.adapter = adapter
            textBox.setText(x)
        })
*/
        var searchMovieItems1 = dbCLient.search("Batman", application, { movieItems ->
            var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieItems)
            listOfItem.adapter = adapter
            listOfItem.setOnItemClickListener { parent, view, position, ld ->
                Toast.makeText(this, movieItems.toString(), Toast.LENGTH_SHORT).show()
            }
        })


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
                                MovieSearchItem.kt(
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
