package yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver.JobUtil
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver.Util
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver.Util.Companion.checkFollowedMovieToSendAReminder

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
        Log.e(TAG, "JobService onStartJob called")
        Toast.makeText(applicationContext, "JobService => onStartJob", Toast.LENGTH_LONG).show()
        var task = JobUtil(this)
        checkFollowedMovieToSendAReminder(Util.context!!, Util.activity!!)


        task.execute(p0)
        return true
    }



}