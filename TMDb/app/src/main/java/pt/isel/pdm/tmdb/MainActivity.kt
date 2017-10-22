package pt.isel.pdm.tmdb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.io.File.separator

class MainActivity : AppCompatActivity() {

    private val requestQueue by lazy { Volley.newRequestQueue(this)}
    private val textBox by lazy { findViewById(R.id.textBox) as TextView }
    //val MovieSearchitem
    private val SEARCH_MOVIE = "https://api.themoviedb.org/3/search/movie?api_key=b531994cdfaa8e3b441e4086b1c6756d&query=Batman"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //textBox = findViewById(R.id.textBox) as TextView

        requestQueue.add(JsonObjectRequest(
                SEARCH_MOVIE,
                null,
                {
                    val jsonSearchMovieItem = it.get("results") as JSONArray


                    Log.i("jsonSearchMovieItem:", jsonSearchMovieItem.toString())
                    val searchMovieItem = jsonSearchMovieItem.asSequence().joinToString (
                        separator = "\n",
                        transform = { "Id:"+ it.getString("id")+ "\t\t" + it.getString("title")  }
                    )

                    textBox.text = searchMovieItem
                    //textBox.setTextSize(50, 2.0f)
                },

                {
                    textBox.text= "ERROR::\n${it.toString()}"
                })

        )
    }


    fun JSONArray.asSequence() =
            (0 until length()).asSequence().map { get(it) as JSONObject }
}
