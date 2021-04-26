package de.lukaskraemer.inkadigiman.alarm


import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.database.Task
import de.lukaskraemer.inkadigiman.data.repository.AppRepository
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList


class AlarmReceiver : BroadcastReceiver() {
    private lateinit var application : Application

    override fun onReceive(context: Context, intent: Intent?) {
        application = context.applicationContext as Application
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pushup", false)) {
            runBlocking() {
                val dateNow = Calendar.getInstance()
                dateNow.set(Calendar.HOUR, 0)
                dateNow.set(Calendar.MINUTE, 0)
                dateNow.set(Calendar.SECOND, 1)
                dateNow.set(Calendar.MILLISECOND, 0)
                sendNotification(context, AppRepository(context.applicationContext as Application).getTaskByDay(dateNow))
            }
        }
    }

    fun sendNotification(context: Context, tasks: List<Task>?) {

        val notifcationList: ArrayList<Notification> = ArrayList()
        tasks?.forEach { it ->
            val time = Calendar.getInstance()
            time.timeInMillis = it.time
            //Parameter for Fragemnt



            val notificationIntent = Intent(context, NotificationActivity::class.java)
            notificationIntent
                    .putExtra("time", it.time.toString())
                    .putExtra("notice", it.notice)
                    .putExtra("type", it.type.toString())
            val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(NotificationActivity::class.java)
            stackBuilder.addNextIntentWithParentStack(notificationIntent)
            val pendingIntent: PendingIntent =
                    stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)


            //Notification
            val newMessageNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(context.resources.getStringArray(R.array.kindoftask)[it.type].toString() + " "+ context.getString(R.string.at) + " " + time.get(Calendar.HOUR).toString()+ ":" + time.get(Calendar.MINUTE))
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setAutoCancel(true)
                    .build()


            notifcationList.add(newMessageNotification)
        }

        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.namen_notification))
                //set content text to support devices running API level < 24
                .setSmallIcon(R.mipmap.ic_launcher)
                //build summary info into InboxStyle template
                //specify which group this notification belongs to
                .setGroup(GROUP_KEY_WORK_EMAIL)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .build()


        NotificationManagerCompat.from(context).apply {
            var i = 0
            notifcationList.forEach {
                notify(i, it)
                i++

            }
            notify(SUMMARY_ID, summaryNotification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "de.lukaskraemer.inka_digiman.channelId"
        private  const val GROUP_KEY_WORK_EMAIL = "de.lukaskraemer.inka_digiman.notfification"
        val SUMMARY_ID = 0

    }


}


