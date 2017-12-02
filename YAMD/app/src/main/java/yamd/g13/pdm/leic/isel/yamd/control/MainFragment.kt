package yamdb.g13.pdm.leic.isel.yamdb.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.*
import yamd.g13.pdm.leic.isel.yamd.model.Movie
import yamd.g13.pdm.leic.isel.yamd.model.MovieDetail
import yamd.g13.pdm.leic.isel.yamd.view.DetailActivity
import yamd.g13.pdm.leic.isel.yamd.view.adapter.MyOnScrollListener


/**
 * Created by tony_mendes on 26/10/2017.
 */


internal var currentMovieDetail : MovieDetail? = null

class MoviesFragment : Fragment() {

    private var gridView: GridView? = null
    private var width: Int = 0
    private val moviesList: ArrayList<Movie> = ArrayList(0)
    private val moviesDetail = HashMap<Int, MovieDetail>()
    private var endpoint = Endpoint.POPULAR
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
        val size = getDisplaySize(activity)

        width = if(isTablet(activity)) size.x / 6 else size.x / 3

        when(isTablet(activity)){
        }

        if (activity != null) {
            gridView = rootView.findViewById(R.id.gridview)
            moviesList.addAll(Command.execute(endpoint))
            gridView!!.adapter = ImageAdapter(activity, moviesList.mapTo(ArrayList<String>(), { movie : Movie -> movie.poster_path }), width)
            gridView!!.columnWidth = width
        }

        //listen for preses on gridView items
        gridView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            var movie = moviesList[position]
            var endpoint = Endpoint.MOVIE_DETAIL
            if(!moviesDetail.containsKey(movie.id)){
                endpoint.extraPath = "${movie.id}"
                moviesDetail[movie.id] = Command.execute<MovieDetail>(endpoint)[0]
            }
            currentMovieDetail = moviesDetail[movie.id]
            startActivity(Intent(activity, DetailActivity::class.java))
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        if (isNetworkAvailable(activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)) {
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


        gridView!!.setOnScrollListener(object : MyOnScrollListener() {
            override fun onLoadMore(pages: Int, totalItemsCount: Int): Boolean {
                var list = Command.execute<Movie>(endpoint)
                if (list.isEmpty())
                    return false
                moviesList.addAll(list)
                (gridView!!.adapter as ImageAdapter).updateAdapter(list.mapTo(ArrayList<String>(), { movie : Movie -> movie.poster_path }))
                return true // ONLY if more data is actually being loaded; false otherwise.
            }
        })
    }

    fun setEndPoint(endpoint: Endpoint){
        this.endpoint = endpoint
    }
}
internal fun setMovieDetailViews(context: Context, activity: Activity){
    if(currentMovieDetail != null){

        var movieDescription = activity.findViewById<TextView>(R.id.movie_overview)
        var size = getDisplaySize(context)
        var imageView = activity.findViewById<ImageView>(R.id.movie_poster)
        var width : Int = (size.x * 0.75).toInt()
        var picasso = Picasso.with(context)
        var path = POSTER_BASE_URL + currentMovieDetail!!.poster_path
        var voteAverage = activity.findViewById<RatingBar>(R.id.movie_vote_average)

        activity.title = currentMovieDetail!!.title
        movieDescription.text = currentMovieDetail!!.overView
        loadImage(picasso, context, path, width, imageView)
        picasso.invalidate(path)
        voteAverage.rating = currentMovieDetail!!.voteAverage.toFloat()
    }
}