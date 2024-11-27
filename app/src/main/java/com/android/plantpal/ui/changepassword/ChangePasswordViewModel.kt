package com.android.plantpal.ui.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.remote.response.ChangeForgotPasswordResponse
import com.android.plantpal.ui.utils.Result

class ChangePasswordViewModel (private val repository: Repository) : ViewModel() {
    fun changeForgotPassword(email: String, password: String): LiveData<Result<ChangeForgotPasswordResponse>> {
        return repository.changeForgotPassword(email, password)
    }
}

