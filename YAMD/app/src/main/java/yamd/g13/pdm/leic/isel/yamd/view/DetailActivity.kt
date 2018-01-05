package yamd.g13.pdm.leic.isel.yamd.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*
import yamd.g13.pdm.leic.isel.yamd.R
import yamd.g13.pdm.leic.isel.yamd.control.Controller
import yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.service.OurJobService

class DetailActivity : AppCompatActivity() {

    var checkBox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        Controller.setMovieDetailViews(this, this)
        //onInsert(Controller.getController())

        checkBox = findViewById<CheckBox>(R.id.follow)
        //var movieDescription = activity.findViewById<TextView>(R.id.movie_overview)
        //var checkBox = follow as CheckBox

        if(checkBox == null) Log.e("ERROR", "dfhgjdskkljfhjksk")

        checkBox!!.setOnClickListener {
            Toast.makeText(this, "checkBos => isChecked: " + checkBox!!.isChecked, Toast.LENGTH_SHORT).show()
            //scheduleJob()
        }

        if(checkBox != null && checkBox!!.isChecked){
            Toast.makeText(this, "IF scheduleJob", Toast.LENGTH_SHORT).show()
            scheduleJob()
        }
    }


    private fun scheduleJob(){
        //tO Use extra
        var extras: PersistableBundle = PersistableBundle()
        extras.putString(OurJobService.extraDATA, "VALUE EXTRA")

        var componentName = ComponentName(this, OurJobService::class.java)
        var jobInfo: JobInfo =  JobInfo.Builder(1, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setExtras(extras)
                .setPeriodic(2000)
                .build()

        var jobScheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)

        Toast.makeText(this, "scheduleJob DONE", Toast.LENGTH_SHORT).show()

    }




}
