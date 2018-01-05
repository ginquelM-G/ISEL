package yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.AsyncTask
import android.os.PersistableBundle
import android.widget.Toast
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.service.OurJobService


/**
 * Created by Ginquel on 28/12/2017.
 */
class Util {

    companion object {

        fun scheduleJob2(context: Context){
            var serviceComponent: ComponentName =  ComponentName(context, OurJobService::class.java)
            var builder: JobInfo.Builder = JobInfo.Builder(0, serviceComponent)
            builder.setMinimumLatency(1 * 1000)
            builder.setOverrideDeadline(3 * 1000)

            var jobscheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
            jobscheduler.schedule(builder.build())

        }


        fun scheduleJob(ctx: Context, activity: Activity){
            System.out.println("HEllo")
            //tO Use extra
            var extras: PersistableBundle = PersistableBundle()
            extras.putString(OurJobService.extraDATA, "VALUE EXTRA")

            var componentName = ComponentName(ctx, OurJobService::class.java)
            var jobInfo: JobInfo =  JobInfo.Builder(1, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setMinimumLatency(1 * 1000)
                    .setOverrideDeadline(3 * 1000)
                    //.setPeriodic(20)
                    .setExtras(extras)
                    .build()

            var jobScheduler: JobScheduler = activity.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)

            Toast.makeText(ctx, "MainActivity scheduleJob DONE", Toast.LENGTH_SHORT).show()

        }
    }
}

/**
**   This class will be responsable to check in the background all the movie that are followed
**   by the user and display the notification if the movie will going to make a debut
*/

class JobUtil : AsyncTask<JobParameters, Unit, Unit>(){

    override fun doInBackground(vararg backgroudParams: JobParameters) {
        var jobParams: JobParameters = backgroudParams[0]
        //var stringData: String = jobParams.extras.getString(OurJobService.extraDATA)




    }

}
