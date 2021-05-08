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
import java.util.Calendar
import kotlin.collections.ArrayList


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pushup", false)) {
            runBlocking {
                val dateNow = Calendar.getInstance()
                dateNow.set(Calendar.HOUR, 0)
                dateNow.set(Calendar.MINUTE, 0)
                dateNow.set(Calendar.SECOND, 1)
                dateNow.set(Calendar.MILLISECOND, 0)
                sendNotification(context, AppRepository(context.applicationContext as Application).getTaskByDay(dateNow))
            }
        }
    }

    private fun sendNotification(context: Context, tasks: List<Task>?) {
        val notifcationList: ArrayList<Notification> = ArrayList()
        tasks?.forEach { it ->
            val time = Calendar.getInstance()
            time.timeInMillis = it.time
            //Parameter for Fragemnt
            time.add(Calendar.MONTH, 1)
            val myIntent = Intent(context, NotificationActivity::class.java)
                    .putExtra("time", it.time.toString())
                    .putExtra("notice", it.notice)
                    .putExtra("type", it.type.toString())

            val intent2 = PendingIntent.getActivity(context, it.id.toInt(), myIntent,
                    PendingIntent.FLAG_ONE_SHOT)

            //Notification
            val newMessageNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(intent2)
                    .setContentTitle(context.resources.getStringArray(R.array.kindoftask)[it.type].toString() + " " + context.getString(R.string.at) + " " + time.get(Calendar.HOUR).toString() + ":" + time.get(Calendar.MINUTE))
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setAutoCancel(true)
                    .build()
            notifcationList.add(newMessageNotification)
        }

        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.namen_notification))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val descriptionText = "notication"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.apply {
                NotificationManagerCompat.from(context).apply {
                    var i = 0
                    notifcationList.forEach {
                        notify(i, it)
                        i++
                    }
                    notify(SUMMARY_ID, summaryNotification)
                }
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "de.lukaskraemer.inka_digiman.channelId"
        private  const val GROUP_KEY_WORK_EMAIL = "de.lukaskraemer.inka_digiman.notfification"
        const val SUMMARY_ID = 0

    }
}


