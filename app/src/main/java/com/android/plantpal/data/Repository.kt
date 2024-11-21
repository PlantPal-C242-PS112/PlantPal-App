package com.android.plantpal.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.android.plantpal.data.preference.UserModel
import com.android.plantpal.data.preference.UserPreference
import com.android.plantpal.data.remote.LoginRequest
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.data.remote.response.RegisterResponse
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