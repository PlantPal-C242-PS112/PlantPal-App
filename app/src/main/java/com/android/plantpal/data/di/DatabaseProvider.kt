package com.android.plantpal.data.di

import android.content.Context
import androidx.room.Room
import com.android.plantpal.data.database.ReminderDatabase

object DatabaseProvider {

    private var instance: ReminderDatabase? = null

    fun getDatabase(context: Context): ReminderDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                ReminderDatabase::class.java,
                "reminder_database"
            ).build()
        }
        return instance!!
    }
}