package yamdb.g13.pdm.leic.isel.yamdb.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import yamd.g13.pdm.leic.isel.yamd.control.POSTER_BASE_URL
import yamd.g13.pdm.leic.isel.yamd.control.loadImage
import java.util.ArrayList

/**
 * Created by tony_mendes on 26/10/2017.
 */
class ImageAdapter(ctx: Context, paths: ArrayList<String>, x: Int) : BaseAdapter() {
    private var mContext: Context? = ctx
    private var posters: ArrayList<String>? = paths
    private var width: Int = x
    private val picasso = Picasso.with(ctx)
    override fun getCount(): Int {
        return posters!!.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {

        val imageView = if (convertView == null) ImageView(mContext) else (convertView as ImageView?)!!
        loadImage(picasso, mContext!!, POSTER_BASE_URL + posters!![position], width, imageView)
        return imageView
    }
    fun updateAdapter(list: ArrayList<String>){
        posters!!.addAll(list)
        notifyDataSetChanged()
    }
}