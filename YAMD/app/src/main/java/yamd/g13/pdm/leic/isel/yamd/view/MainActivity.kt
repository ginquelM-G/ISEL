package yamd.g13.pdm.leic.isel.yamd.view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.Endpoint
import yamd.g13.pdm.leic.isel.yamd.control.onNavigationItemSelected

class MainActivity : AppCompatActivity() {
//https://www.youtube.com/watch?v=EZ-sNN7UWFU
    private val itemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_popular -> {
                onNavigationItemSelected(Endpoint.POPULAR, supportFragmentManager)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_now_playing -> {
                onNavigationItemSelected(Endpoint.NOW_PLAYING, supportFragmentManager)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_upcoming -> {
                onNavigationItemSelected(Endpoint.UPCOMING, supportFragmentManager)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onNavigationItemSelected(Endpoint.POPULAR, supportFragmentManager)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(itemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        var item : MenuItem = menu!!.findItem(R.id.search_action)
        var searchView : SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query == null || query.isEmpty())
                    return false
                onNavigationItemSelected(Endpoint.SEARCH.setQueryString(query), supportFragmentManager)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == null || newText.isEmpty())
                    return false
                onNavigationItemSelected(Endpoint.SEARCH.setQueryString(newText), supportFragmentManager)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.about_action){
            startActivity(Intent(this, AboutActivity::class.java))
        }
        return true
    }

}

