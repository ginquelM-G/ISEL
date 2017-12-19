package yamd.g13.pdm.leic.isel.yamd.control

import android.app.Activity
import android.app.FragmentController
import android.content.*
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.commands.Command
import yamd.g13.pdm.leic.isel.yamd.control.provider.EndpointBox
import yamd.g13.pdm.leic.isel.yamd.control.provider.MoviesContract
import yamd.g13.pdm.leic.isel.yamd.model.Movie
import yamd.g13.pdm.leic.isel.yamd.model.MovieDetail
import yamdb.g13.pdm.leic.isel.yamdb.view.ImageAdapter
import yamdb.g13.pdm.leic.isel.yamdb.view.MainFragment

/**
 * Created by tony_mendes on 12/11/2017.
 */

object Controller{

    val API_BASE_URL: String = "http://api.themoviedb.org/3"
    val API_KEY: String = "fa47a597365f6c8543d8ed92dbb2028f"
    val POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/"
    val CURRENT_FRAGMENT_KEY = "MAIN_FRAGMENT"
    var currentFragment : MainFragment? = null
    val ENDPOINT_KEY = "ENDPOINT"
    val cache = HashMap<Endpoint, List<Movie>>()
    val moviesDetail = HashMap<Int, MovieDetail>()
    var currentEndpoint = Endpoint.POPULAR
    var currentMovieDetail : MovieDetail? = null

    val MOVIES_LST_LOADER = 1
    val MOVIE_ID_LOADER = 2
    val DETAILS_LST_LOADER = 3
    val DETAIL_ID_LOADER = 4
    val CATEGORIES_LST_LOADER = 5
    val CATEGORY_ID_LOADER = 6

    val fragmentsMap = HashMap<Endpoint, MainFragment>()

    var contentResolver : ContentResolver? = null

    fun init(contentResolver : ContentResolver, contentValues: ContentValues){
        this.contentResolver = contentResolver
        /*var categoryList = ArrayList<Category>(0)
        categoryList.add(Category("popular films", "most viewed movies"))
        categoryList.add(Category("up coming", "movies that will be in debut soon"))
        categoryList.add(Category("now playing", "movies in theaters"))

        DbAdapter.writeToCategories(contentResolver, contentValues, categoryList)*/
    }

    fun getCursorLoader(ctx: Context,id: Int, args: Bundle?): Loader<Cursor> {
        return when (id) {
            MOVIES_LST_LOADER ->{
                if (args!=null){
                    var endpointBox = args.getParcelable<EndpointBox>(ENDPOINT_KEY)
                    onNavigationItemSelected(endpointBox.endpoint, endpointBox.supportFragmentManager)
                }
                CursorLoader(
                        ctx, MoviesContract.Movies.CONTENT_URI, MoviesContract.Movies.PROJECT_ALL,
                        null,null,null
                )
            }
            MOVIE_ID_LOADER ->CursorLoader(
                    ctx, MoviesContract.Movies.CONTENT_URI, MoviesContract.Movies.PROJECT_ALL,
                    null,null,null
            )
            DETAILS_LST_LOADER ->CursorLoader(
                    ctx, MoviesContract.Details.CONTENT_URI, MoviesContract.Details.PROJECT_ALL,
                    null,null,null
            )
            CATEGORY_ID_LOADER ->CursorLoader(
                    ctx, MoviesContract.Details.CONTENT_URI, MoviesContract.Details.PROJECT_ALL,
                    null,null,null
            )
            CATEGORIES_LST_LOADER ->
                CursorLoader(
                        ctx, MoviesContract.Categories.CONTENT_URI, MoviesContract.Categories.PROJECT_ALL,
                        null,null,null
                )
            DETAIL_ID_LOADER ->
                CursorLoader(
                        ctx, MoviesContract.Categories.CONTENT_URI, MoviesContract.Categories.PROJECT_ALL,
                        null,null,null
                )
            else ->
                throw IllegalArgumentException("unknown id: $id")
        }
    }

    fun getDataFromCursor(loader: Loader<Cursor>?, data: Cursor?) {
         when (loader!!.id) {
            MOVIES_LST_LOADER ->{}
            MOVIE_ID_LOADER ->{}
            DETAILS_LST_LOADER ->{
            }
            CATEGORY_ID_LOADER ->{

            }
            CATEGORIES_LST_LOADER ->{
                var s = ""
                while (data!!.moveToNext()){
                    s = data.getString(2)
                }
                print(s)
            }
            DETAIL_ID_LOADER ->{}
            else ->
                throw IllegalArgumentException("unknown id: $loader!!.id")
        }
    }

    fun resizeDrawable(ctx: Context, image: Drawable): Drawable {
        val b = (image as BitmapDrawable).bitmap
        val bitmapResized = Bitmap.createScaledBitmap(b, 10, (10 * 1.5).toInt(), false)
        return BitmapDrawable(ctx!!.resources, bitmapResized)
    }

    fun getDisplaySize(ctx : Context) : Point{
        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    fun isTablet(ctx: Context) : Boolean{
        var xLarge  = (ctx.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) == 4
        var large = (ctx.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_LONG_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
        return  xLarge || large
    }

    fun onNavigationItemSelected(endpoint: Endpoint, fragmentManager: FragmentManager){
        if (!fragmentsMap.containsKey(endpoint))
            fragmentsMap[endpoint] = MainFragment()
        currentFragment = fragmentsMap[endpoint]
        currentEndpoint = endpoint
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, currentFragment)
                .commit()
    }

    fun restoreState(fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, currentFragment)
                .commit()
    }
    fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun loadImage(picasso: Picasso, context: Context, path: String, width: Int, imageView: ImageView) {
        try {
            val drawable = resizeDrawable(context!!, context!!.resources.getDrawable(R.drawable.placeholder, context!!.resources.newTheme()))
            picasso.load(path)
                    .resize(width, (width * 1.5).toInt()).placeholder(drawable).into(imageView)
            picasso.invalidate(path)
        }catch (e:Exception){}
    }

    fun getMoviesPosters():List<String>{
        if(!cache.containsKey(currentEndpoint)){
            cache[currentEndpoint] = ArrayList(0)
            (cache[currentEndpoint] as ArrayList<Movie>).addAll(Command.execute(currentEndpoint))
        }
        if(!cache.containsKey(currentEndpoint)){
            cache[currentEndpoint] = ArrayList(0)
            (cache[currentEndpoint] as ArrayList<Movie>).addAll(Command.execute(currentEndpoint))
        }

       return cache[currentEndpoint]!!.mapTo(ArrayList<String>(), { movie : Movie -> movie.poster_path })
    }

    fun getMoreMoviesPosters():List<String>{
        var list = Command.execute<Movie>(Controller.currentEndpoint)
        (Controller.cache[Controller.currentEndpoint] as ArrayList<Movie>).addAll(list)
        return list.mapTo(ArrayList<String>(), { movie : Movie -> movie.poster_path })
    }

    fun obtainMovieDetail(position:Int){
        var movie = cache[currentEndpoint]!![position]
        var endpoint = Endpoint.MOVIE_DETAIL
        if(!moviesDetail.containsKey(movie.id)){
            endpoint.extraPath = "${movie.id}"
            moviesDetail[movie.id] = Command.execute<MovieDetail>(endpoint)[0]
        }
        currentMovieDetail = moviesDetail[movie.id]
    }

    fun setMovieDetailViews(context: Context, activity: Activity){
        if(Controller.currentMovieDetail != null){

            var movieDescription = activity.findViewById<TextView>(R.id.movie_overview)
            var size = Controller.getDisplaySize(context)
            var imageView = activity.findViewById<ImageView>(R.id.movie_poster)
            var width : Int = (size.x * 0.75).toInt()
            var picasso = Picasso.with(context)
            var path = Controller.POSTER_BASE_URL + Controller.currentMovieDetail!!.poster_path
            var voteAverage = activity.findViewById<RatingBar>(R.id.movie_vote_average)

            activity.title = Controller.currentMovieDetail!!.title
            movieDescription.text = Controller.currentMovieDetail!!.overView
            Controller.loadImage(picasso, context, path, width, imageView)
            picasso.invalidate(path)
            voteAverage.rating = Controller.currentMovieDetail!!.voteAverage.toFloat()
            onInsert(Controller)
        }
    }

    public fun getController() : Controller{
        return Controller
    }

    fun onInsert(controller: Controller) {
        //Toast.makeText(this, "BEGIN Insert in DB movieDetails", Toast.LENGTH_LONG).show()

        var d = controller.currentMovieDetail
        var details = MovieDetail(d!!.id, d.title , d.voteAverage, d.voteCount, d.popularity,
        d.poster_path, d.overView , d.releaseDate)

        val movieDatails = ContentValues()
        movieDatails.put(MoviesContract.Details.MOVIE_ID, d!!.id)
        movieDatails.put(MoviesContract.Details.TITLE,  d.title)

        movieDatails.put(MoviesContract.Details.VOTE_AVERAGE, d!!.voteAverage.toFloat())
        movieDatails.put(MoviesContract.Details.VOTE_COUNT, d!!.voteCount)
        movieDatails.put(MoviesContract.Details.POPULARITY, d!!.popularity)
        movieDatails.put(MoviesContract.Details.POSTER_PATH, d!!.poster_path)
        movieDatails.put(MoviesContract.Details.OVERVIEW, d!!.overView)
        movieDatails.put(MoviesContract.Details.RESLEASE_DATE, d!!.releaseDate)

        controller.contentResolver!!.insert(
                MoviesContract.Details.CONTENT_URI,
                movieDatails
        )

        //Toast.makeText(this, "Insert in DB movieDetails", Toast.LENGTH_LONG).show()
        Log.i("Inserted movie ", controller.currentMovieDetail!!.title +" into db")

    }



}

