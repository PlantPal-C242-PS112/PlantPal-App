package com.android.plantpal.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.android.plantpal.data.preference.UserModel
import com.android.plantpal.data.preference.UserPreference
import com.android.plantpal.data.remote.ChangeForgotPasswordRequest
import com.android.plantpal.data.remote.LoginRequest
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.data.remote.SendOtpRequest
import com.android.plantpal.data.remote.VerifyOtpRequest
import com.android.plantpal.data.remote.response.ChangeForgotPasswordResponse
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.data.remote.response.RegisterResponse
import com.android.plantpal.data.remote.response.SendOtpResponse
import com.android.plantpal.data.remote.response.VerifyOtpResponse
import com.android.plantpal.data.remote.retrofit.ApiService
import com.android.plantpal.ui.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class Repository (
    private val userPreference: UserPreference,
    private val apiService: ApiService
    ) {

    fun register(username: String,
                 email: String,
                 password: String,
                 fullname: String
    ): LiveData<Result<RegisterResponse>> = liveData(
        Dispatchers.IO
    ) {
        emit(Result.Loading)
        try {
            val request = RegisterRequest(
                username = username,
                email = email,
                password = password,
                fullname = fullname
            )
            val response = apiService.register(request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
            Log.d("RegisterData", "Error: ${e.message}")
        }
    }

    fun login(identifier: String, password: String): LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val request = LoginRequest(
                    identifier = identifier,
                    password = password,
                )
                val response = apiService.login(request)

                if (response.status) {
                    val id = response.data.id.toString()
                    val user = UserModel(
                        userId = id,
                        name = response.data.username,
                        token = response.data.token
                    )

                    saveSession(user)
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error("Login failed: ${response.message}"))
                }
            } catch (e: Exception) {
                emit(Result.Error("Error: ${e.message}"))
            }
        }

    fun sendEmailVerificationOtp(email: String): LiveData<Result<SendOtpResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val request = SendOtpRequest(email = email)
            val response = apiService.sendEmailVerificationOtp(request)
            if (response.status) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
            Log.e("SendEmailVerification", "Error: ${e.message}", e)
        }
    }

    fun verifyEmailVerificationOtp(email: String, otp: String): LiveData<Result<VerifyOtpResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val request = VerifyOtpRequest(email = email, otp = otp)
            val response = apiService.verifyEmailVerificationOtp(request)
            if (response.status) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
            Log.e("VerifyEmailOtp", "Error: ${e.message}", e)
        }
    }

    fun sendForgotPasswordOtp(email: String): LiveData<Result<SendOtpResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val request = SendOtpRequest(email = email)
            val response = apiService.sendForgotPasswordOtp(request)
            if (response.status) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
            Log.e("sendForgotPasswordOtpData", "Error: ${e.message}", e)
        }
    }

    fun verifyForgotPasswordOtp(email: String, otp: String): LiveData<Result<VerifyOtpResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val request = VerifyOtpRequest(email = email, otp = otp)
            val response = apiService.verifyForgotPasswordOtp(request)
            if (response.status) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message ?: "Password Changed Sucessfully"))
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
            Log.e("VerifyForgotPasswordOtpData", "Error: ${e.message}", e)
        }
    }


    fun changeForgotPassword(email: String, password : String): LiveData<Result<ChangeForgotPasswordResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val request = ChangeForgotPasswordRequest(
                email = email,
                password = password,
            )
            val response = apiService.changeForgotPassword(request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
            Log.d("ChangeForgotPasswordData", "Error: ${e.message}")

        }
    }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    companion object {
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService
        ) = Repository(preference, apiService)
    }
}