package de.lukaskraemer.inkadigiman.alarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.lukaskraemer.inkadigiman.MainActivity
import de.lukaskraemer.inkadigiman.R
import java.util.*


class NotificationActivity : AppCompatActivity() {
    private lateinit var type: TextView
    private lateinit var timeView: TextView
    private lateinit var date: TextView
    private lateinit var notice: TextView
    private lateinit var btn: Button

    private lateinit var context: Context

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val timestamp= intent?.getStringExtra("time")!!.toLong()
        val typeid= intent.getStringExtra("type")!!.toInt()
        val noticeRaw = intent.getStringExtra("notice")
        val time = Calendar.getInstance()
        time.timeInMillis = timestamp
        type.text = context.getString(R.string.Notification_Appointment_type,context.resources.getStringArray(R.array.kindoftask)[typeid].toString())

        notice.text = context.getString(R.string.note) +" : " + noticeRaw
        timeView.text = context.getString(R.string.Notification_time, time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE))
        date.text =  context.getString(R.string.Notification_date, time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.MONTH), time.get(Calendar.YEAR))
    }
 override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.notification_activity)

     type = findViewById(R.id.notification_type)
     date = findViewById(R.id.notification_date)
     timeView = findViewById(R.id.notification_time)
     notice = findViewById(R.id.notification_notice)
     btn = findViewById(R.id.notification_btn)
     context=this.applicationContext

     onNewIntent(intent)

     btn.setOnClickListener {
         startActivity(Intent(this, MainActivity::class.java))
     }
 }
}