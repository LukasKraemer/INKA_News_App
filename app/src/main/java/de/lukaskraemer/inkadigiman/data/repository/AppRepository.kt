package de.lukaskraemer.inkadigiman.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import de.lukaskraemer.inkadigiman.data.APIHandler
import de.lukaskraemer.inkadigiman.data.database.Task
import de.lukaskraemer.inkadigiman.data.database.TaskDao
import de.lukaskraemer.inkadigiman.data.database.TaskDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AppRepository(application: Application) {
    private val taskDao: TaskDao
    private val apiHandler: APIHandler
    init {
        val db = TaskDataBase.createInstance(application)
        taskDao = db.taskDao
        apiHandler = APIHandler(application.baseContext)
    }

    // Implement all functions
    suspend fun insert(task: Task) {
        withContext(Dispatchers.IO)
        {

            taskDao.insert(task)
            //apiHandler.create(task)
        }
    }

    suspend fun delete(task: Task) {
        withContext(Dispatchers.IO)
        {
            taskDao.delete(task)
        }
    }

    suspend fun update(task: Task) {
        withContext(Dispatchers.IO)
        {
            taskDao.update(task)
            //apiHandler.change(task.id , task)
        }
    }

    suspend fun getTaskById(taskId: Long): Task? {
        var task: Task?
        withContext(Dispatchers.IO)
        {
            task = taskDao.getTaskById(taskId)
        }
        return task
    }

    suspend fun getTaskByDay(time:Calendar): List<Task>? {
        var task: List<Task>?
        time.set(Calendar.HOUR_OF_DAY, 0)
        time.set(Calendar.MINUTE, 0)
        val end: Calendar = time.clone() as Calendar
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        withContext(Dispatchers.IO)
        {
            task = taskDao.getTaskByDay(time.timeInMillis, end.timeInMillis)
        }

        return task
    }

    suspend fun getAllTasks(): List<Task>? {
        var task: List<Task>?
        withContext(Dispatchers.IO)
        {
            task = taskDao.getTaskList()
        }
        return task
    }

    fun getLiveHiddenDataTask(): LiveData<List<Task>> {
        return taskDao.getLiveHiddenDataTaskList()
    }

    suspend fun updatewholeDatabase() {
        withContext(Dispatchers.IO) {
            val newValues = apiHandler.getAll()
            newValues.forEach {
                if (it != null) {
                    val task = taskDao.getTaskById(it.id)
                    if (task == null) {
                        taskDao.insert(it)
                    } else {
                        task.id = it.id
                        taskDao.update(task)
                    }
                }
            }
        }
    }

    fun getLiveTaskListbyDay(calendar: Calendar): LiveData<List<Task>> {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val end: Calendar = calendar.clone() as Calendar
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)

        return taskDao.getLiveTaskListbyDay(calendar.timeInMillis, end.timeInMillis)
    }


    fun getLiveDataTask(): LiveData<List<Task>> {
        return taskDao.getLiveDataTaskList()
    }
    fun getLiveDataTaskbyId(): LiveData<List<Task>> {
        return taskDao.getLiveDataTaskList()
    }

    fun dates(): LiveData<List<Task>> {
        return taskDao.dates()
    }
}