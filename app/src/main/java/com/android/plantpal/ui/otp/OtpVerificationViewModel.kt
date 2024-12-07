package com.android.plantpal.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.remote.response.VerifyOtpResponse
import com.android.plantpal.ui.utils.Result

class OtpVerificationViewModel(private val repository: Repository) : ViewModel() {
    fun verifyForgotPasswordOtp(email: String, otp: String): LiveData<Result<VerifyOtpResponse>> {
        return repository.verifyForgotPasswordOtp(email, otp)
    }
}