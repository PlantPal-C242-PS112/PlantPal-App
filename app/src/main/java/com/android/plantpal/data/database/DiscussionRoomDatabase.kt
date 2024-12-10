package com.android.plantpal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.plantpal.data.remote.response.ListItemDiscussions

@Database(
    entities = [ListItemDiscussions::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class DiscussionRoomDatabase : RoomDatabase() {
    abstract fun discussionDao(): DiscussionDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: DiscussionRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): DiscussionRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DiscussionRoomDatabase::class.java, "plantpal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}