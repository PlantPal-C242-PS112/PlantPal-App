package com.android.plantpal.data.remote.retrofit

import com.android.plantpal.data.remote.ChangeForgotPasswordRequest
import com.android.plantpal.data.remote.LoginRequest
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.data.remote.SendOtpRequest
import com.android.plantpal.data.remote.VerifyOtpRequest
import com.android.plantpal.data.remote.response.AddPlantResponse
import com.android.plantpal.data.remote.response.ChangeForgotPasswordResponse
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.data.remote.response.RegisterResponse
import com.android.plantpal.data.remote.response.SendOtpResponse
import com.android.plantpal.data.remote.response.UserPlantsResponse
import com.android.plantpal.data.remote.response.VerifyOtpResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("users/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("users/register/send-otp")
    suspend fun sendEmailVerificationOtp(
        @Body sendOtpRequest: SendOtpRequest
    ): SendOtpResponse

    @POST("users/register/verify-otp")
    suspend fun verifyEmailVerificationOtp(
        @Body verifyOtpRequest: VerifyOtpRequest
    ): VerifyOtpResponse

    @POST("users/forgot-password/send-otp")
    suspend fun sendForgotPasswordOtp(
        @Body sendOtpRequest: SendOtpRequest
    ): SendOtpResponse

    @POST("users/forgot-password/verify-otp")
    suspend fun verifyForgotPasswordOtp(
        @Body verifyOtpRequest: VerifyOtpRequest
    ): VerifyOtpResponse

    @PUT("users/forgot-password/change-password")
    suspend fun changeForgotPassword(
        @Body changeForgotPasswordRequest: ChangeForgotPasswordRequest
    ): ChangeForgotPasswordResponse

    @GET("user-plants")
    suspend fun getUserPlants(
        @Header("Authorization") token: String
    ): UserPlantsResponse

    @POST("user-plants")
    suspend fun addPlant(
        @Header ("Authorization") token: String
    ): AddPlantResponse


}

