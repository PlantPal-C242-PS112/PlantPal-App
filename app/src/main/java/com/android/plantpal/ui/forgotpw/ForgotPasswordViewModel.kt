package com.android.plantpal.ui.forgotpw

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.remote.response.SendOtpResponse
import com.android.plantpal.ui.utils.Result

class ForgotPasswordViewModel(private val repository: Repository) : ViewModel() {
    fun forgotPassword(email: String): LiveData<Result<SendOtpResponse>> {
        return repository.sendForgotPasswordOtp(email)
    }
}


