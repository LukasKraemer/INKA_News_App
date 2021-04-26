package de.lukaskraemer.inkadigiman


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import de.lukaskraemer.inkadigiman.alarm.AlarmReceiver
import de.lukaskraemer.inkadigiman.data.repository.AppRepository
import de.lukaskraemer.inkadigiman.ui.updateValues.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.INTERNET",
            "android.permission.RECEIVE_BOOT_COMPLETED",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.WRITE_CALENDAR"

        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        askPermissions()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnLongClickListener {
            sendEmail()
            true
        }

        fab.setOnClickListener {
            val dialog = Dialog()
            dialog.show(supportFragmentManager, "New Input")
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_add_task, R.id.nav_showtask, R.id.nav_add_hidden, R.id.nav_calendar
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        alarmananager()

    }

    private fun alarmananager() {

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pushup", false)) {

            val stunde = try {
                PreferenceManager.getDefaultSharedPreferences(this).getString("pushuptime", "")!!
                    .replace("Uhr".toRegex(), "").replace("AM".toRegex(), "")
                    .replace("PM".toRegex(), "").replace(":".toRegex(), "")
                    .replace("\\s".toRegex(), "").toInt() / 100
            } catch (e: Exception) {
                6
            }
            val minute = try {
                PreferenceManager.getDefaultSharedPreferences(this).getString("pushupmin", "")!!
                    .replace("min".toRegex(), "").replace("\\s".toRegex(), "").toInt()
            } catch (e: Exception) {
                0
            }

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val notificationIntent = Intent(this, AlarmReceiver::class.java)
            val broadcast = PendingIntent.getBroadcast(
                this,
                100,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, stunde)
                set(Calendar.MINUTE, minute)
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                broadcast
            )

        }
    }

    private fun sendEmail() {

        val to = "lukas.kraemer@srhk.de"
        val subject = getString(R.string.subject_mail)
        val message = ""

        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        email.putExtra(Intent.EXTRA_SUBJECT, subject)
        email.putExtra(Intent.EXTRA_TEXT, message)
        email.type = "message/rfc822"

        startActivity(Intent.createChooser(email, getString(R.string.email_client_choose)))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}

