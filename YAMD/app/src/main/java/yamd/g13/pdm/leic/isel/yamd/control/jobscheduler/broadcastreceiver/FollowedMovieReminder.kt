package yamd.g13.pdm.leic.isel.yamd.control.jobscheduler.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import yamd.g13.pdm.leic.isel.yamd.control.notification.FollowMovieNotification

class FollowedMovieReminder : BroadcastReceiver() {

    override fun onReceive(ctx: Context, intent: Intent) {

        FollowMovieNotification.notify(ctx, "Simple notification tests", 0)
    }
}
