package yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver.JobUtil
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver.Util

import java.net.URI

/**
 *  Created by User01 on 28/12/2017.
 */
class OurJobService : JobService() {

    companion object {
        var extraDATA: String = "EXTRA1"
    }

    private val TAG : String = "OurJobService"


    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.i(TAG, "JobService onStopJob called")
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.i(TAG, "JobService onStartJob called")
        Toast.makeText(applicationContext, "onStartJob", Toast.LENGTH_LONG).show()
        var task:  AsyncTask<JobParameters, Unit, Unit>  = object :  AsyncTask<JobParameters, Unit, Unit>(){

            override fun doInBackground(vararg backgroudParams: JobParameters) {

                var jobParams: JobParameters = backgroudParams[0]
                var stringData: String = jobParams.extras.getString(OurJobService.extraDATA)
//                Toast.makeText(applicationContext, stringData,Toast.LENGTH_LONG ).show()
//                Toast.makeText(applicationContext, "BACKGROUND",Toast.LENGTH_LONG ).show()


                jobFinished(jobParams, true)

            }


        }

        var util: Util = Util()

        task.execute(p0)


        return true
    }


    fun retrieveMovieInfo(){
        val URL: String = "content://.."
        val uri: Uri = Uri.parse(URL)
        //var cursor: Cursor = managedQuery

        val query: String = "SELECT * FROM MOVIEPcOMMING"
        var  db: SQLiteDatabase = SQLiteDatabase.openDatabase(URL, null,0) //= readbile

        var cursor: Cursor = db.rawQuery(query, null)
        var data: String? = null

        if(cursor.moveToFirst()){
            do{

            }while (cursor.moveToNext())
        }
        db.close()
    }
}