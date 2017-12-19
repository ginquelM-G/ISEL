package yamdb.g13.pdm.leic.isel.yamdb.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.*
import yamd.g13.pdm.leic.isel.yamd.view.DetailActivity
import yamd.g13.pdm.leic.isel.yamd.view.adapter.MyOnScrollListener
import java.util.ArrayList


/**
 * Created by tony_mendes on 26/10/2017.
 */

//https://futurestud.io/tutorials/how-to-save-and-restore-the-scroll-position-and-state-of-a-android-listview


class MainFragment() : Fragment(), Parcelable {

    private var gridView: GridView? = null
    private var width: Int = 0
    private var size: Point? = null

    private val CURREN_VIEW_POSITION_KEY = "CURREN_VIEW_INDEX"
    private val CURREN_VIEW_TOP_KEY = "CURREN_VIEW_TOP"
    @SuppressLint("ValidFragment")
    constructor(parcel: Parcel) : this() {
        width = parcel.readInt()
        size = parcel.readParcelable(Point::class.java.classLoader)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)

        size = Controller.getDisplaySize(activity)
        width = if(Controller.isTablet(activity)) size!!.x / 3 else size!!.x / 3

        if (activity != null) {
            gridView = rootView.findViewById(R.id.gridview)
            gridView!!.columnWidth = width
        }

        gridView!!.adapter = ImageAdapter(activity, Controller.getMoviesPosters() as java.util.ArrayList<String>, width)

        if (savedInstanceState != null){
            var position = savedInstanceState!!.getInt(CURREN_VIEW_POSITION_KEY)
            var top = savedInstanceState!!.getInt(CURREN_VIEW_TOP_KEY)

            gridView!!.setSelectionFromTop(position, top)
        }

        //listen for preses on gridView items
        gridView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            Controller.obtainMovieDetail(position)
            startActivity(Intent(activity, DetailActivity::class.java))
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        gridView!!.visibility = GridView.VISIBLE

        /*
        if (Controller.isNetworkAvailable(activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)) {
            gridView!!.visibility = GridView.VISIBLE
        } else {
            val textView1 = TextView(activity)
            val layout1 = activity.findViewById<RelativeLayout>(R.id.relativelayout)
            textView1.text = "You are not connected to the Internet"
            if (layout1.childCount == 1) {
                layout1.addView(textView1)
            }
            gridView!!.visibility = GridView.GONE
        }
*/

        gridView!!.setOnScrollListener(object : MyOnScrollListener() {
            override fun onLoadMore(pages: Int, totalItemsCount: Int): Boolean {
                var list = Controller.getMoreMoviesPosters()
                if (list.isEmpty())
                    return false
                (gridView!!.adapter as ImageAdapter).updateAdapter(list as ArrayList<String>)
                return true // ONLY if more data is actually being loaded; false otherwise.
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        //https://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
        var position = gridView!!.firstVisiblePosition
        var view = gridView!!.getChildAt(position)
        var top = if(view == null) 0 else view.top - gridView!!.top
        outState!!.putInt(CURREN_VIEW_POSITION_KEY, position)
        outState!!.putInt(CURREN_VIEW_TOP_KEY, top)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(width)
        parcel.writeParcelable(size, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainFragment> {
        override fun createFromParcel(parcel: Parcel): MainFragment {
            return MainFragment(parcel)
        }

        override fun newArray(size: Int): Array<MainFragment?> {
            return arrayOfNulls(size)
        }
    }
}