package yamd.g13.pdm.leic.isel.yamd.view

import android.app.LoaderManager
import android.content.ContentValues
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.*
import yamd.g13.pdm.leic.isel.yamd.control.provider.EndpointBox
import yamdb.g13.pdm.leic.isel.yamdb.view.MainFragment

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        //adapter.changeCursor(null)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        Controller.init(contentResolver, ContentValues())
        return Controller.getCursorLoader(this, id, args)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        Controller.getDataFromCursor(loader, data)
        loaderManager.destroyLoader(Controller.MOVIES_LST_LOADER)
        data!!.close()
    }

    private fun getBundle(endpoint: Endpoint) : Bundle{
        var box = EndpointBox(endpoint, supportFragmentManager)
        var b = Bundle()
        b.putParcelable(Controller.ENDPOINT_KEY, box)
        return  b
    }

    //https://www.youtube.com/watch?v=EZ-sNN7UWFU
    //https://www.youtube.com/watch?v=54UmVL7Fn0U&list=PLfuE3hOAeWhYCPPLA75AXfd0pILeyePjv
    private val itemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_popular -> {
                loaderManager.initLoader(Controller.MOVIES_LST_LOADER, getBundle(Endpoint.POPULAR), this)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_now_playing -> {
                loaderManager.initLoader(Controller.MOVIES_LST_LOADER, getBundle(Endpoint.NOW_PLAYING), this)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_upcoming -> {
                loaderManager.initLoader(Controller.MOVIES_LST_LOADER, getBundle(Endpoint.UPCOMING), this)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loaderManager.initLoader(Controller.CATEGORIES_LST_LOADER, null, this)

        if (savedInstanceState == null)
            loaderManager.initLoader(Controller.MOVIES_LST_LOADER, getBundle(Endpoint.NOW_PLAYING), this)
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
                Controller.onNavigationItemSelected(Endpoint.SEARCH.setQueryString(query), supportFragmentManager)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == null || newText.isEmpty())
                    return false
                //onNavigationItemSelected(Endpoint.SEARCH.setQueryString(newText), supportFragmentManager)
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putParcelable(Controller.CURRENT_FRAGMENT_KEY, Controller.currentFragment)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Controller.currentFragment = savedInstanceState!!.getParcelable<MainFragment>(Controller.CURRENT_FRAGMENT_KEY)
        Controller.restoreState(supportFragmentManager)
    }

}

