package de.lukaskraemer.inkadigiman

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import de.lukaskraemer.inkadigiman.data.database.Task
import de.lukaskraemer.inkadigiman.data.repository.AppRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CommonViewModel (application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    ////////////////////////////////////////////////////////////
    // Repository:
    private val repository = AppRepository(application)
    private var liveTaskList = repository.getLiveDataTask()
    private var dates = repository.dates()
    private var liveHiddenTaskList = repository.getLiveHiddenDataTask()


    val allowedtypes: Array<String> = arrayOf ("schooling","class work", "test", "presentation", "customer visit", "others" )

    ////////////////////////////////////////////////////////////
    // Methods to interact with the repository:
    fun insert(type: Int,time:Calendar, note: String) {
        viewModelScope.launch {
            val task = Task(id = 0L, type = type, time = time.timeInMillis , notice = note)
            repository.insert(task)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun getTaskById(taskId: Long): Task? {
        var task: Task? = null
        viewModelScope.launch {
            task = repository.getTaskById(taskId)
        }
        return task

    }

    suspend fun getTaskByDay(calendar: Calendar): List<Task>? {
        return repository.getTaskByDay(calendar)
    }

    fun getAllTasks(): List<Task>? {
        var tasks: List<Task>? = null
        viewModelScope.launch {
            tasks = repository.getAllTasks()
        }
        return tasks
    }

    ////////////////////////////////////////////////////////////
    // Getters for LiveData
    fun getLiveTaskList(): LiveData<List<Task>> = liveTaskList
    fun dates(): LiveData<List<Task>> = dates

    fun getLiveTaskById(id: Long): LiveData<List<Task>> = liveTaskList

    ////////////////////////////////////////////////////////////
    // Utils

    fun getLiveHiddenTaskList(): LiveData<List<Task>> = liveHiddenTaskList

    fun getLiveTaskListbyDay(time: Calendar): LiveData<List<Task>> = repository.getLiveTaskListbyDay(time)


}