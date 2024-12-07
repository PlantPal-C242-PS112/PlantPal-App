package com.android.plantpal.data.di

import android.content.Context
import com.android.plantpal.data.Repository
import com.android.plantpal.data.database.DiscussionRoomDatabase
import com.android.plantpal.data.preference.UserPreference
import com.android.plantpal.data.preference.dataStore
import com.android.plantpal.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = DiscussionRoomDatabase.getDatabase(context)
        return Repository.getInstance(pref, apiService, database)
    }
}