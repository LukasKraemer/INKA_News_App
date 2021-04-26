package de.lukaskraemer.inkadigiman.data


import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.database.Task
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit


class APIHandler(val context: Context) {
    var responsestring: String = ""
    private lateinit var mySearchUrl: HttpUrl
    private val usersettings = PreferenceManager.getDefaultSharedPreferences(context)
    private val nameofTasks = "task"

    private var url: String = if (usersettings.getString(
            "ip",
            ""
        ) == ""
    ) (context.getString(R.string.ipDefault)) else (usersettings.getString("ip", "")!!)
    private val credential = Credentials.basic(
        usersettings.getString("username", "")!!,
        usersettings.getString("password", "")!!,
        Charsets.UTF_8
    )
    private val protokoll = if (usersettings.getBoolean("ssl", true)) ("https") else ("http")


    private val client = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private fun getTaskList(): List<Task?> {
        val json = this.responsestring
        val gson = Gson()
        val listType: Type? = object : TypeToken<ArrayList<Task?>?>() {}.type
        val tasklist: List<Task> = gson.fromJson(json, listType)
        return tasklist
    }


    private fun initrespone(response: String) {
        try {
            this.responsestring = response
            println(responsestring)
        } catch (e: Exception) {
            this.responsestring = "error init"
        }

    }


    fun getAll(): List<Task?> {
        mySearchUrl = HttpUrl.Builder()
            .scheme(protokoll)
            .host(url)
            .addPathSegment(nameofTasks)
            .build()
        val request = defaulResquest()
        request.get()

        send(request.build())


        return getTaskList()
    }

    fun checklogin(id: String): Boolean {
        mySearchUrl = HttpUrl.Builder()
            .scheme(protokoll)
            .host(url)
            .addPathSegment(nameofTasks)
            .addPathSegment(id)
            .build()
        val request = defaulResquest()
        request.get()
        send(request.build())
        return true
    }

    fun create(values: Task): Boolean {
        mySearchUrl = HttpUrl.Builder()
            .scheme("https")
            .host(url)
            .addPathSegment(nameofTasks)
            .build()

        val formBody = defaultFormbody(values)
        val request = defaulResquest()
        request.put(formBody)

        send(request.build())
        return true
    }

    fun change(id: Long, values: Task): String {
        mySearchUrl = HttpUrl.Builder()
            .scheme(protokoll)
            .host(url)
            .addQueryParameter(nameofTasks, id.toString())
            .build()

        val formBody = defaultFormbody(values)
        val request = defaulResquest()
        request.post(formBody)

        send(request.build())
        return responsestring
    }


    private fun send(request: Request) {
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                initrespone(response.body!!.string())

            }


            override fun onFailure(call: Call, e: IOException) {
                println("fehler")
                e.printStackTrace()

            }
        }

        )
    }

    private fun defaulResquest(): Request.Builder {

        return Request.Builder()
            .url(mySearchUrl)
            .header("User-Agent", context.getString(R.string.app_name))
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", getLanguage())
            .addHeader("Authorization", credential)
            .addHeader("Connection", "keep-alive")
            .addHeader("Accept-Charset", Charsets.UTF_8.toString())
    }

    fun defaultFormbody(values: Task): RequestBody {
        return FormBody.Builder()
            .add("type", values.type.toString())
            .add("time", values.time.toString())
            .add("notice", values.notice)
            .build()
    }

    private fun getLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().toLanguageTags()
        } else {
            Locale.getDefault().language
        }
    }
}

