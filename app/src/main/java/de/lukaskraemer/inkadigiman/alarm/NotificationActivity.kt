package de.lukaskraemer.inkadigiman.alarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import de.lukaskraemer.inkadigiman.CommonViewModel
import de.lukaskraemer.inkadigiman.MainActivity
import de.lukaskraemer.inkadigiman.MainViewModelFactory
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.database.Task
import de.lukaskraemer.inkadigiman.data.database.TaskDataBase
import de.lukaskraemer.inkadigiman.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*


class NotificationActivity : AppCompatActivity() {
    private lateinit var type: TextView
    private lateinit var date: TextView
    private lateinit var timeView: TextView
    private lateinit var notice: TextView
    private lateinit var btn: Button

    private lateinit var context: Context


 override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.notification_activity)

     //dirty buit it works ;-)

     type = findViewById(R.id.notification_type)
     date = findViewById(R.id.notification_date)
     timeView = findViewById(R.id.notification_time)
     notice = findViewById(R.id.notification_notice)
     btn = findViewById(R.id.notification_btn)
     context=this.applicationContext

     val timestamp= intent.getStringExtra("time")!!.toLong()
     val typeid= intent.getStringExtra("type")!!.toInt()
     val noticeRaw = intent.getStringExtra("notice")
     val time = Calendar.getInstance()
     time.timeInMillis = timestamp


     type.text = context.getString(R.string.Appointment_type) +" : " + context.resources.getStringArray(R.array.kindoftask)[typeid].toString()
     date.text = context.getString(R.string.date) +" : " + time.get(Calendar.HOUR).toString()+ ":" + time.get(Calendar.MINUTE)
     notice.text = context.getString(R.string.note) +" : " + noticeRaw

     btn.setOnClickListener {
         startActivity(Intent(this, MainActivity::class.java))

     }
 }


}