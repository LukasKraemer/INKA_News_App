package de.lukaskraemer.inkadigiman.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var time: Long,
    var notice: String,
    var type: Int,
    var show: Boolean = true
)



