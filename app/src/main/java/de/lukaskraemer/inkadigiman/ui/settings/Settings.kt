package de.lukaskraemer.inkadigiman.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.preference.*
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.DataValidator
import kotlin.math.absoluteValue

class Settings: PreferenceFragmentCompat() {
    //userdata
    private var preusername: EditTextPreference? = null
    private var prepassword: EditTextPreference? = null

    private var premail: SwitchPreference? = null
    private var prepushup: SwitchPreference? = null
    private var prepushuptime: ListPreference? = null
    private var prepushuptimemin: ListPreference? = null

    private var preip: EditTextPreference? = null
    private var pretoken: EditTextPreference? = null
    private var pressl: SwitchPreference? = null

    //values:
    private lateinit var sharedPreference: SharedPreferences
    private var username = ""
    private var password = ""

    private var mail = false
    private var pushup = false
    private var pushuptime = 6
    private var pushuptimemin = 0

    private var ip = ""
    private var token = ""
    private var ssl = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings, rootKey)

        preusername = findPreference("username")
        prepassword = findPreference("password")
        premail = findPreference("mail")
        prepushup = findPreference("pushup")
        prepushuptime = findPreference("pushuptime")
        prepushuptimemin = findPreference("pushupmin")
        preip = findPreference("ip")
        pretoken = findPreference("token")
        pressl = findPreference("ssl")



        loadDatafromPreferences()
        preusername?.setOnBindEditTextListener { editText ->
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        prepassword?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        }

        preip?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_TEXT
        }
        pretoken?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        }





        prepushuptime?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        prepushuptimemin?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        preip?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            if (TextUtils.isEmpty(text)) {
                getString(R.string.noipset)
            } else {
                val checker = DataValidator()
                if (checker.isValidHostname(text) || checker.isValidIP(text)) {
                    text
                } else {
                    getString(R.string.wrongvalue, text)
                }
            }

        }


        preusername?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    getString(R.string.noemailset)
                } else {
                    val checker = DataValidator()
                    if (checker.isValidEmail(text)) {
                        text
                    } else {
                        getString(R.string.wrongvalue, text)
                    }
                }

            }


        prepassword?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    getString(R.string.notpwdset)
                } else {
                    "*".repeat(text.length)
                }

            }
        pretoken?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            if (TextUtils.isEmpty(text)) {
                getString(R.string.notokenset)
            } else {
                "*".repeat(text.length)
            }

        }
    }

    private fun loadDatafromPreferences() {
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())

        username = sharedPreference.getString("username", "")!!
        password = sharedPreference.getString("password", "")!!
        mail = sharedPreference.getBoolean("mail", false)
        pushup = sharedPreference.getBoolean("pushup", false)
        pushuptime = sharedPreference.getString("pushuptime", "")!!.replace("Uhr".toRegex(), "")
            .replace("AM".toRegex(), "").replace("PM".toRegex(), "").replace(":".toRegex(), "")
            .replace("\\s".toRegex(), "").toInt() / 100
        pushuptimemin = sharedPreference.getString("pushupmin", "")!!.replace("min".toRegex(), "")
            .replace("\\s".toRegex(), "").toInt() / 100
        ip = sharedPreference.getString("ip", "")!!
        token = sharedPreference.getString("username", "")!!
        ssl = sharedPreference.getBoolean("ssl", true)


    }

}


