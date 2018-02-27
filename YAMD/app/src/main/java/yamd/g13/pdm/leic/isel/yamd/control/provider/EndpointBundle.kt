package yamd.g13.pdm.leic.isel.yamd.control.provider

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.FragmentManager
import yamd.g13.pdm.leic.isel.yamd.control.Endpoint

@SuppressLint("ParcelCreator")
/**
 * Created by tony_mendes on 17/12/2017.
 */
class EndpointBundle(val endpoint: Endpoint, val supportFragmentManager: FragmentManager): Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }
}