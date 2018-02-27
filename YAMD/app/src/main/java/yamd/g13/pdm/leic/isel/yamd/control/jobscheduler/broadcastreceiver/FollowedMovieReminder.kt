package yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.util.Log
import android.widget.Toast
import yamd.g13.pdm.leic.isel.yamd.control.Controller
import yamd.g13.pdm.leic.isel.yamd.control.notification.FollowMovieNotification
import yamd.g13.pdm.leic.isel.yamd.control.provider.MoviesContract
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FollowedMovieReminder : BroadcastReceiver() {

    override fun onReceive(ctx: Context, intent: Intent) {
        Toast.makeText(ctx, "FollowedMovieReminder => onReceive", Toast.LENGTH_LONG).show()
        Log.e("FollowedMovieReminder", " => onReceive START")
        //FollowMovieNotification.notify(ctx, "Simple notification tests", 0)
        try {
            var cursor = retrieveMovieFollowed(Controller)

            //val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateFormat = SimpleDateFormat("YYYY-MM-DD")
            var calendar = Calendar.getInstance()
            var todayDateTxt = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.DAY_OF_MONTH)}"
            Log.e("DATEs before parse", "Today11 " + todayDateTxt )
            val todayDate = dateFormat.parse(todayDateTxt)

            Log.e("DATEs After parse", "Today" + todayDate.toString())

            while (cursor.moveToNext()){
                Log.e("DATEs before", "Retrieve" + cursor.getString(2).toString())
                //var  dateFormat =  SimpleDateFormat("YYYY-MM-DD")
                var movieReleaseDate = dateFormat.parse( cursor.getString(2)) as Date
                Log.e("DATEs before", "TTTTTTTTTTTTTTTTTTT" + movieReleaseDate.toString())

                val difference = Math.abs(movieReleaseDate.time - todayDate.time)
                val differenceDates = difference / (24 * 60 * 60 * 1000)
                Log.e("DATEs before", "TTTTTTTTTTTTTTTTTTT" + movieReleaseDate.toString())
                Toast.makeText(ctx, "RELEASE: "+ movieReleaseDate + "\nCurr : "+todayDate, Toast.LENGTH_LONG).show()
                Log.e("DATEs", "RELEASE: "+ movieReleaseDate + "\nCurr : "+todayDate)
                if(differenceDates < 100 &&  differenceDates >= 0){
                    //Util.checkFollowedMovieToSendAReminder(Util.context!!, Util.activity!!)
                    FollowMovieNotification.notify(ctx, "Simple notification tests", 0)
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR: ", "class FollowedMovieReminder:\n ${e.message}")
        }

    }


    fun retrieveMovieFollowed(controller: Controller): Cursor {
        var d = controller.currentMovieDetail
        var mProjection = arrayOf("id", "title", "release_date", "follow")
        val mSelectionArgs = arrayOf("")  // Initializes an array to contain selection arguments
        val mSelection = "follow = 1"
        Log.e("Foooll", " => retrieveMovieFollowed START")
        var cursor = controller.contentResolver!!.query(MoviesContract.DetailsTable.CONTENT_URI, mProjection, mSelection, null, null, null)

        cursor.moveToNext()
        Log.e("Foooll", cursor.getString(2).toString())

        return cursor
    }
}
