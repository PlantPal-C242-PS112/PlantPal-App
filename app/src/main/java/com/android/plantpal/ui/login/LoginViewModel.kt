package com.android.plantpal.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.ui.utils.Result

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun login(identifier: String, password: String): LiveData<Result<LoginResponse>> {
        return repository.login(identifier, password)
    }

}