package com.android.plantpal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.plantpal.data.Repository
import com.android.plantpal.data.preference.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}