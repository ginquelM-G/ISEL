package yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 *  Created by Ginquel on 28/12/2017.
 */
class JobScheduler : BroadcastReceiver() {

    private val TAG : String = "OurJobService"

    override fun onReceive(ctx: Context?, intent: Intent?) {
        Log.i(TAG, "Broadcast receiver OnReceiver calledwith intent $intent")
        Toast.makeText(ctx, "JobScheduler inside onReceiver", Toast.LENGTH_LONG).show()
        var ctx1 : Context? = ctx
        //Util.scheduleJob(ctx)
    }
}