package com.android.plantpal.data.remote.retrofit

import com.android.plantpal.data.remote.LoginRequest
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("users/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}