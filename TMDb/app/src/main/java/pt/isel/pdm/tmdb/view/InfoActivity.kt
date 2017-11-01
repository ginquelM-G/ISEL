package pt.isel.pdm.tmdb.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import pt.isel.pdm.tmdb.R
import pt.isel.pdm.tmdb.R.id.tv_movieDetails

/**
 * Created by User01 on 01/11/2017.
 */
class InfoActivity: AppCompatActivity() {

    private var tv_info: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movies_details)

        tv_info =  findViewById(R.id.tv_movieDetails) as TextView

        var info = "\nAuthours: \nGinquel Moreira" + "\nTony Mendes" +
                "\n\nData source: \nThe Movie Database \nhttps://www.themoviedb.org"
        tv_info?.text = info



    }
}

