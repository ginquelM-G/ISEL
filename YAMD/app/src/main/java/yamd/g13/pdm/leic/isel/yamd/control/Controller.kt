package yamd.g13.pdm.leic.isel.yamd.control

import android.app.Activity
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
import android.widget.*
import com.squareup.picasso.Picasso
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.commands.Command
import yamd.g13.pdm.leic.isel.yamd.control.provider.DbAdapter
import yamd.g13.pdm.leic.isel.yamd.control.provider.EndpointBundle
import yamd.g13.pdm.leic.isel.yamd.control.provider.MoviesContract
import yamd.g13.pdm.leic.isel.yamd.model.Movie
import yamd.g13.pdm.leic.isel.yamd.model.MovieDetail
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

    var currentEndpointBundle: EndpointBundle? = null

    val MOVIES_LST_LOADER = 1
    val MOVIE_ID_LOADER = 2
    val DETAILS_LST_LOADER = 3
    val DETAIL_ID_LOADER = 4
    val CATEGORIES_LST_LOADER = 5
    val CATEGORY_ID_LOADER = 6

    val POPULAR_MOVIES_LOADER = 7
    val UPCAMING_MOVIES_LOADER = 8
    val NOW_PLAYING_MOVIES_LOADER = 9

    val fragmentsMap = HashMap<Endpoint, MainFragment>()

    var contentResolver : ContentResolver? = null
    var contentValues: ContentValues? = null

    val POPULAR_MOVIES = "popular"
    val UPCOMING_MOVIES = "nowPlaying"
    val NOW_PLAYING_MOVIES = "upcoming"

    fun init(contentResolver : ContentResolver, contentValues: ContentValues, args: Bundle?){

        if (args!=null){
            currentEndpointBundle = args.getParcelable<EndpointBundle>(ENDPOINT_KEY)
        }

        this.contentResolver = contentResolver
        this.contentValues = contentValues
        /*
        var categoryList = ArrayList<Category>(0)
        categoryList.add(Category(POPULAR_MOVIES, "most viewed movies"))
        categoryList.add(Category(UPCOMING_MOVIES, "movies that will be in debut soon"))
        categoryList.add(Category(NOW_PLAYING_MOVIES, "movies in theaters"))

        DbAdapter.writeToCategories(contentResolver, contentValues, categoryList)*/
    }

    fun getCursorLoader(ctx: Context,id: Int, args: Bundle?): Loader<Cursor> {

        var selectionArgs =  when (id) {
            POPULAR_MOVIES_LOADER -> arrayOf(POPULAR_MOVIES)
            NOW_PLAYING_MOVIES_LOADER -> arrayOf(NOW_PLAYING_MOVIES)
            else -> arrayOf(UPCOMING_MOVIES)
        }
        return when (id) {

            POPULAR_MOVIES_LOADER, NOW_PLAYING_MOVIES_LOADER, UPCAMING_MOVIES_LOADER->{
                CursorLoader(
                        ctx, MoviesContract.MoviesTable.CONTENT_URI, MoviesContract.MoviesTable.PROJECT_ALL,
                        MoviesContract.MoviesTable.CATEGORY + "=?", selectionArgs,null
                )
            }

            MOVIE_ID_LOADER ->CursorLoader(
                    ctx, MoviesContract.MoviesTable.CONTENT_URI, MoviesContract.MoviesTable.PROJECT_ALL,
                    null,null,null
            )


            DETAILS_LST_LOADER ->CursorLoader(
                    ctx, MoviesContract.DetailsTable.CONTENT_URI, MoviesContract.DetailsTable.PROJECT_ALL,
                    null,null,null
            )
            CATEGORY_ID_LOADER ->CursorLoader(
                    ctx, MoviesContract.DetailsTable.CONTENT_URI, MoviesContract.DetailsTable.PROJECT_ALL,
                    null,null,null
            )
            CATEGORIES_LST_LOADER ->
                CursorLoader(
                        ctx, MoviesContract.CategoriesTable.CONTENT_URI, MoviesContract.CategoriesTable.PROJECT_ALL,
                        null,null,null
                )
            DETAIL_ID_LOADER ->
                CursorLoader(
                        ctx, MoviesContract.CategoriesTable.CONTENT_URI, MoviesContract.CategoriesTable.PROJECT_ALL,
                        null,null,null
                )
            else ->
                throw IllegalArgumentException("unknown id: $id")
        }
    }

    fun getDataFromCursor(loader: Loader<Cursor>?, data: Cursor?) {
         when (loader!!.id) {
             POPULAR_MOVIES_LOADER, NOW_PLAYING_MOVIES_LOADER, UPCAMING_MOVIES_LOADER->{
                var result = DbAdapter.readMoviesFromCursor(loader, data)
                 if (!result.isEmpty()) {
                     cache[currentEndpointBundle!!.endpoint] = result
                 }
                 if (currentEndpointBundle != null){
                     onNavigationItemSelected(currentEndpointBundle!!.endpoint,
                             currentEndpointBundle!!.supportFragmentManager)
                 }
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

    fun saveDataToDataBase(contentResolver: ContentResolver, contentValues: ContentValues){
        if(cache.containsKey(Endpoint.POPULAR)){
            DbAdapter.writeToMovies(contentResolver, contentValues, cache[Endpoint.POPULAR]!!)
        }
        if(cache.containsKey(Endpoint.NOW_PLAYING)){
            DbAdapter.writeToMovies(contentResolver, contentValues, cache[Endpoint.NOW_PLAYING]!!)
        }
        if(cache.containsKey(Endpoint.UPCOMING)){
            DbAdapter.writeToMovies(contentResolver, contentValues, cache[Endpoint.UPCOMING]!!)
        }
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
        /*if(!cache.containsKey(currentEndpoint)){
            cache[currentEndpoint] = DbAdapter.readMovies(contentResolver!!, contentValues!!)
            (cache[currentEndpoint] as ArrayList<Movie>).addAll(Command.execute(currentEndpoint))
        }*/
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
            if(currentFragment == fragmentsMap[Endpoint.UPCOMING] || currentFragment == fragmentsMap[Endpoint.NOW_PLAYING]) {

                val checkBox = activity.findViewById<CheckBox>(R.id.follow)
                var isToFollowTheMovie = false

                //Toast.makeText(activity, "AAA ${isToFollowTheMovie}", Toast.LENGTH_LONG).show()
                checkBox.setOnClickListener(followOnClickListener(checkBox, activity))
                insertMoviesDetailsInDB(context, activity, Controller, isToFollowTheMovie)
            }


        }
    }


    fun insertMoviesDetailsInDB(context: Context, activity: Activity, controller: Controller, isToFollowTheMovie: Boolean) {
        var d = controller.currentMovieDetail

        var mProjection = arrayOf("id", "title")
        val mSelectionArgs = arrayOf("")  // Initializes an array to contain selection arguments
        val mSelection = "id = ${d!!.id}"
        var cursor = controller.contentResolver!!.query(MoviesContract.DetailsTable.CONTENT_URI, mProjection, mSelection, null, null, null)

        if(cursor != null){
            try{
                if(cursor.count > 0) {
                    Toast.makeText(activity, "Already inserted in DB (EXIST!!!)", Toast.LENGTH_LONG).show()
                    cursor.moveToNext()
                    Toast.makeText(activity, "=> " + cursor.getString(0).toString(),  Toast.LENGTH_LONG).show()
                    Toast.makeText(activity,"Title: " + cursor.getString(1).toString(), Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(activity, "Insert in DB", Toast.LENGTH_LONG).show()
                    var d = controller.currentMovieDetail
                    val movieDatails = ContentValues()
                    movieDatails.put(MoviesContract.DetailsTable.MOVIE_ID, d!!.id)
                    movieDatails.put(MoviesContract.DetailsTable.TITLE,  d.title)
                    movieDatails.put(MoviesContract.DetailsTable.VOTE_AVERAGE, d!!.voteAverage.toFloat())
                    movieDatails.put(MoviesContract.DetailsTable.VOTE_COUNT, d!!.voteCount)
                    movieDatails.put(MoviesContract.DetailsTable.POPULARITY, d!!.popularity)
                    movieDatails.put(MoviesContract.DetailsTable.POSTER_PATH, d!!.poster_path)
                    movieDatails.put(MoviesContract.DetailsTable.OVERVIEW, d!!.overView)
                    movieDatails.put(MoviesContract.DetailsTable.RELEASE_DATE, d!!.releaseDate)
                    movieDatails.put(MoviesContract.DetailsTable.FOLLOW, d!!.isToFollowMovie)

                    controller.contentResolver!!.insert(
                            MoviesContract.DetailsTable.CONTENT_URI,
                            movieDatails
                    )
               }

            }catch (e: Exception){
                Toast.makeText(activity, "ERROR ${e.message}", Toast.LENGTH_LONG).show()

            }
        }
    }


    fun insertMoviesDetailsInDB2(context: Context, activity: Activity, controller: Controller, isToFollowTheMovie: Boolean) {
        var d = controller.currentMovieDetail

        var mProjection = arrayOf("id", "poster_path", "title", "vote_average", "vote_count", "popularity", "overview", "release_date", "follow")
        val mSelectionArgs = arrayOf("")  // Initializes an array to contain selection arguments
        val mSelection = "id = ${d!!.id}"
        var cursor = controller.contentResolver!!.query(MoviesContract.DetailsTable.CONTENT_URI, mProjection, mSelection, null, null, null)

        if(cursor != null){
            try{
                if(cursor.count > 0) {
                    cursor.moveToNext()
                    //controller.contentResolver!!.delete( MoviesContract.DetailsTable.CONTENT_URI, mSelection, null)
                    Toast.makeText(activity, "=> " + cursor.getString(0).toString(), Toast.LENGTH_LONG).show()
                    Toast.makeText(activity, "Title: " + cursor.getString(1).toString(), Toast.LENGTH_LONG).show()

                    var d = controller.currentMovieDetail
                    val movieDatails = ContentValues()
                    movieDatails.put(MoviesContract.DetailsTable.MOVIE_ID, cursor.getString(0).toString())
                    movieDatails.put(MoviesContract.DetailsTable.POSTER_PATH, cursor.getString(1).toString())
                    movieDatails.put(MoviesContract.DetailsTable.TITLE, cursor.getString(2).toString())
                    movieDatails.put(MoviesContract.DetailsTable.VOTE_AVERAGE, cursor.getString(3).toString())
                    movieDatails.put(MoviesContract.DetailsTable.VOTE_COUNT, cursor.getString(4).toString())
                    movieDatails.put(MoviesContract.DetailsTable.POPULARITY, cursor.getString(5).toString())
                    movieDatails.put(MoviesContract.DetailsTable.OVERVIEW, cursor.getString(6).toString())
                    movieDatails.put(MoviesContract.DetailsTable.RELEASE_DATE, cursor.getString(7).toString())
                    movieDatails.put(MoviesContract.DetailsTable.FOLLOW, isToFollowTheMovie)

                    controller.contentResolver!!.insert(
                            MoviesContract.DetailsTable.CONTENT_URI,
                            movieDatails
                    )
                }

            }catch (e: Exception){
                Toast.makeText(activity, "ERROR ${e.message}", Toast.LENGTH_LONG).show()

            }
        }
    }


    fun updateMoviesDetailsInDB(controller: Controller, isToFollowTheMovie: Boolean) {
        var d = controller.currentMovieDetail
        Log.e("GIN", d!!.id.toString())
        val mSelection = "id = ${d!!.id}"
        var contentValue = ContentValues()
        contentValues!!.put("follow", 1)
        var cursor1 = this.contentResolver!!.update(MoviesContract.DetailsTable.CONTENT_URI, contentValue, mSelection, null)

//        var cursor1 = controller.contentResolver!!.update(MoviesContract.DetailsTable.CONTENT_URI, contentValue, mSelection, null)
    }



    class followOnClickListener(var checkBox: CheckBox, var activity: Activity) : View.OnClickListener{
        override fun onClick(p0: View?) {
            if (checkBox != null) {
                Toast.makeText(activity, "I'm following this Movie?? ${checkBox!!.isChecked}", Toast.LENGTH_SHORT).show()
                //updateMoviesDetailsInDB(Controller, checkBox!!.isChecked)
                insertMoviesDetailsInDB2(activity, activity, Controller, true)
            }


        }

    }

}

