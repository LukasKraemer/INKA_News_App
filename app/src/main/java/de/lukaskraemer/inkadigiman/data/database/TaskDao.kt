package de.lukaskraemer.inkadigiman.data.database


import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TaskDao
{
    @Insert()
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM Task WHERE id = :taskid")
    suspend fun getTaskById(taskid:Long): Task?

    @Query("SELECT * FROM Task WHERE time >= :start and time <= :end and show =1 order by time asc")
    suspend fun getTaskByDay(start: Long, end: Long): List<Task>


    @Query("SELECT * FROM Task order by time asc")
    suspend fun getTaskList():List<Task>

    @Query("SELECT * FROM Task where show = 1 order by time asc")
    fun getLiveDataTaskList():LiveData<List<Task>>

    @Query("SELECT * FROM Task where show = 0 order by time asc")
    fun getLiveHiddenDataTaskList():LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE time >= :start and time <= :end and show =1 order by time asc")
    fun getLiveTaskListbyDay(start: Long, end: Long):LiveData<List<Task>>

    @Query("select * from Task where show =1")
    fun dates():LiveData<List<Task>>
}

