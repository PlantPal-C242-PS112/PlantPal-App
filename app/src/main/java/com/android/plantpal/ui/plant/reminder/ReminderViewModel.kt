package com.android.plantpal.ui.plant.reminder

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.plantpal.data.database.ReminderDao
import com.android.plantpal.data.database.ReminderDatabase
import com.android.plantpal.data.di.DatabaseProvider
import com.android.plantpal.data.remote.ReminderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val reminderDao: ReminderDao = ReminderDatabase.getDatabase(application).reminderDao()

    fun getAllReminders(): LiveData<List<ReminderEntity>> {
        return reminderDao.getAllReminders()
    }
    fun markAsDone(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminder.isDone = true
            reminderDao.updateReminder(reminder)
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderDao.deleteReminder(reminder)
        }
    }
}

