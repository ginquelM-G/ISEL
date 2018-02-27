package yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import yamd.g13.pdm.leic.isel.yamd.control.Controller
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.service.OurJobService
import yamd.g13.pdm.leic.isel.yamd.control.notification.FollowMovieNotification
import yamd.g13.pdm.leic.isel.yamd.control.provider.MoviesContract
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException




/**
 * Created by Ginquel on 28/12/2017.
 */
class Util {

    companion object {
        var context : Context? = null
        var activity : Activity? = null

        fun scheduleJob2(context: Context){
            var serviceComponent: ComponentName =  ComponentName(context, OurJobService::class.java)
            var builder: JobInfo.Builder = JobInfo.Builder(0, serviceComponent)
            builder.setMinimumLatency(1 * 1000)
            builder.setOverrideDeadline(3 * 1000)

            var jobscheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
            jobscheduler.schedule(builder.build())

        }


        fun scheduleJob(ctx: Context, activity: Activity){
            //FollowMovieNotification.notify(ctx, "Simple notification tests", 0)
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


        fun checkFollowedMovieToSendAReminder(ctx: Context, activity: Activity){
            Toast.makeText(ctx, "Util => Using Alarm Manager", Toast.LENGTH_LONG).show()
            Log.e("Util", " => checkFollowedMovieToSendAReminder START")
            var intent  = Intent(activity, FollowedMovieReminder::class.java)
            intent.putExtra("KEY", "VALUE")

            var pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            var alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            var currentTimeInMilliseconds = SystemClock.elapsedRealtime()
            val ONE_DAY = 24 * 60 * 60* 1000
            val ONE_HOUR = 60 * 60* 1000
            val DEZ_SEGUNDOS = 2 * 1000

            var notifyTime = currentTimeInMilliseconds + DEZ_SEGUNDOS
            //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, notifyTime, DEZ_SEGUNDOS.toLong(), pendingIntent)
            //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, notifyTime, DEZ_SEGUNDOS.toLong(), pendingIntent)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, notifyTime, pendingIntent)
        }

    }
}

/**
**   This class will be responsable to check DB in the background all the movie that are followed
**   by the user and display the notification if the movie will going to make a debut
*/

class JobUtil(var jobService: OurJobService) : AsyncTask<JobParameters, Unit, Unit>(){



        override fun doInBackground(vararg backgroudParams: JobParameters) {

            var jobParams: JobParameters = backgroudParams[0]
            var stringData: String = jobParams.extras.getString(OurJobService.extraDATA)
//                Toast.makeText(applicationContext, stringData,Toast.LENGTH_LONG ).show()
//                Toast.makeText(applicationContext, "BACKGROUND",Toast.LENGTH_LONG ).show()

            //var cursor = retrieveMovieFollowed(controller)
            jobService.jobFinished(jobParams, true)
        }





}
