package pt.isel.pdm.tmdb

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by User01 on 22/10/2017.
 */


class UsingVolleyApp : Application() {

    val requestQueue by lazy { Volley.newRequestQueue(this)}

    override fun onCreate() {
        super.onCreate()
    }
}

val Application.requestQueue : RequestQueue
    get() = (this as UsingVolleyApp).requestQueue