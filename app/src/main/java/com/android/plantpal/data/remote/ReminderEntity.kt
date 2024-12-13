package com.android.plantpal.data.remote

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val time: Long,
    var isDone: Boolean = false,
    val plantId: Int,
    val imageResId: Int
)
