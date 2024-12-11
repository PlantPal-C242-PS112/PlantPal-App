package com.android.plantpal.ui.plant.reminder

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.plantpal.data.database.ReminderDao
import com.android.plantpal.data.database.ReminderDatabase
import com.android.plantpal.data.di.DatabaseProvider
import com.android.plantpal.data.remote.ReminderEntity
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val reminderDao: ReminderDao = ReminderDatabase.getDatabase(application).reminderDao()

    fun getAllReminders(): LiveData<List<ReminderEntity>> {
        val remindersLiveData = MutableLiveData<List<ReminderEntity>>()
        viewModelScope.launch {
            remindersLiveData.postValue(reminderDao.getAllReminders())
        }
        return remindersLiveData
    }

    fun updateReminderStatus(reminderId: Long, status: Boolean) {
        viewModelScope.launch {
            reminderDao.updateIsDone(reminderId, status)
        }
    }
}

