package yamd.g13.pdm.leic.isel.yamd.view

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.Controller
import yamd.g13.pdm.leic.isel.yamd.control.provider.MoviesContract
import yamd.g13.pdm.leic.isel.yamd.model.MovieDetail

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        Controller.setMovieDetailViews(this, this)
        //onInsert(Controller.getController())
    }



}
