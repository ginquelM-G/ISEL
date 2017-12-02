package yamd.g13.pdm.leic.isel.yamd.control

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.support.v4.app.FragmentManager
import android.view.WindowManager
import android.widget.ImageView
import com.squareup.picasso.Picasso
import yamd.g13.pdm.leic.isel.yamd.R
import yamdb.g13.pdm.leic.isel.yamdb.view.MoviesFragment

/**
 * Created by tony_mendes on 12/11/2017.
 */

internal val API_BASE_URL: String = "http://api.themoviedb.org/3"
internal val API_KEY: String = "fa47a597365f6c8543d8ed92dbb2028f"
internal val POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/"

internal val MOVIE_KEY : String = "movies"


internal fun getDisplaySize(ctx : Context) : Point{
    val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

internal fun isTablet(ctx: Context) : Boolean{
    var xLarge  = (ctx.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) == 4
    var large = (ctx.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_LONG_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
    return  xLarge || large
}

internal fun resizeDrawable(ctx: Context, image: Drawable): Drawable {
    val b = (image as BitmapDrawable).bitmap
    val bitmapResized = Bitmap.createScaledBitmap(b, 10, (10 * 1.5).toInt(), false)
    return BitmapDrawable(ctx!!.resources, bitmapResized)
}

internal fun onNavigationItemSelected(endpoint: Endpoint, fragmentManager: FragmentManager){
    var mf = MoviesFragment()
    mf.setEndPoint(endpoint)
    fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mf)
            .commit()
}


internal fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

internal fun loadImage(picasso: Picasso, context: Context, path: String, width: Int, imageView: ImageView){
    val drawable = resizeDrawable(context!!, context!!.resources.getDrawable(R.drawable.placeholder, context!!.resources.newTheme()))
    picasso.load(path)
            .resize(width, (width * 1.5).toInt()).placeholder(drawable).into(imageView)
    picasso.invalidate(path)
}

