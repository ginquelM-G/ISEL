package pt.isel.pdm.tmdb.view

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import pt.isel.pdm.tmdb.R
import pt.isel.pdm.tmdb.data.TheMovieDbClient

/**
 * Created by User01 on 25/10/2017.
 */
class ResultsActivity: AppCompatActivity() {

    private val listOfItem by lazy { findViewById(R.id.list_itens) as ListView }
    private val dbCLient = TheMovieDbClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var movieName: String = intent?.getStringExtra("MOVIE_NAME")?:"empty"

        if("empty".equals(movieName) || "".equals(movieName)){
            Log.e("MOVIE_NAME", "IGUAL A NULL")
            dbCLient.movieNowPlaying(application, { movieItems ->
                var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieItems)
                listOfItem.adapter = adapter
                listOfItem.setOnItemClickListener { parent, view, position, ld ->
                    Toast.makeText(this, movieItems.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
        else {
            dbCLient.search(movieName, application, { movieItems ->
                var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieItems)
                listOfItem.adapter = adapter
                listOfItem.setOnItemClickListener { parent, view, position, ld ->
                    Toast.makeText(this, movieItems.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}