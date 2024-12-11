package com.android.plantpal.data.remote.retrofit

import com.android.plantpal.data.remote.AddPlantRequest
import com.android.plantpal.data.remote.ChangeForgotPasswordRequest
import com.android.plantpal.data.remote.CommentRequest
import com.android.plantpal.data.remote.DeletePlantRequest
import com.android.plantpal.data.remote.LoginRequest
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.data.remote.SendOtpRequest
import com.android.plantpal.data.remote.VerifyOtpRequest
import com.android.plantpal.data.remote.response.AddPlantResponse
import com.android.plantpal.data.remote.response.ChangeForgotPasswordResponse
import com.android.plantpal.data.remote.response.CommentResponse
import com.android.plantpal.data.remote.response.CreateCommentResponse
import com.android.plantpal.data.remote.response.CreateDiscussionResponse
import com.android.plantpal.data.remote.response.CultivationTipsResponse
import com.android.plantpal.data.remote.response.DeleteDiscussionResponse
import com.android.plantpal.data.remote.response.DeletePlantResponse
import com.android.plantpal.data.remote.response.DetailDiscussionResponse
import com.android.plantpal.data.remote.response.DetailDiseaseResponse
import com.android.plantpal.data.remote.response.DiagnosisResponse
import com.android.plantpal.data.remote.response.DetailPlantsResponse
import com.android.plantpal.data.remote.response.DiagnosisHistoryResponse
import com.android.plantpal.data.remote.response.DiscussionResponse
import com.android.plantpal.data.remote.response.DiseaseResponse
import com.android.plantpal.data.remote.response.LikeOrDislikeResponse
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.data.remote.response.PlantsResponse
import com.android.plantpal.data.remote.response.RegisterResponse
import com.android.plantpal.data.remote.response.SendOtpResponse
import com.android.plantpal.data.remote.response.UpdateProfileResponse
import com.android.plantpal.data.remote.response.UserDetailResponse
import com.android.plantpal.data.remote.response.UserPlantsResponse
import com.android.plantpal.data.remote.response.VerifyOtpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("users/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("discussions")
    suspend fun getAllDiscussions(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): DiscussionResponse

    @GET("users")
    suspend fun getUserDetails() : UserDetailResponse

    @GET("plants")
    suspend fun getAllPlants() : PlantsResponse

    @GET("discussions/{id}")
    suspend fun getDetailDiscussion(
        @Path("id") id: Int
    ): DetailDiscussionResponse

    @Multipart
    @POST("discussions")
    suspend fun addDiscussions(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part media: MultipartBody.Part,
        @Part("plant_id") plant_id: RequestBody
    ): CreateDiscussionResponse

    @DELETE("discussions/{id}")
    suspend fun deleteDiscussion(
        @Path("id") id: Int
    ): DeleteDiscussionResponse

    @GET("discussions/{id}/comments")
    suspend fun getComments(
        @Path("id") id: Int
    ): CommentResponse

    @POST("discussions/{id}/comments")
    suspend fun addComments(
        @Path("id") id: Int,
        @Body commentRequest: CommentRequest
    ): CreateCommentResponse


    @Multipart
    @PUT("users/update-profile")
    suspend fun updateProfile(
        @Part profile_picture: MultipartBody.Part?,
        @Part("fullname") fullname: RequestBody
    ): UpdateProfileResponse


    @POST("discussions/{id}/like")
    suspend fun likeOrDislike(
        @Path("id") id: Int
    ): LikeOrDislikeResponse

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

    @GET("diseases")
    suspend fun getAllDiseases() : DiseaseResponse

    @GET("diseases/{id}")
    suspend fun getDetailDisease(
        @Path("id") id: Int
    ): DetailDiseaseResponse

    @Multipart
    @POST("diagnosis")
    suspend fun getDiagnosis(
        @Part image: MultipartBody.Part
    ): DiagnosisResponse
  
    @GET("plants/{id}")
    suspend fun getDetailPlants(
        @Path("id") id: Int
    ): DetailPlantsResponse

    @GET("plants/{id}/cultivation-tips")
    suspend fun getCultivationTips(
        @Path("id") id: Int
    ): CultivationTipsResponse

    @GET("user-plants")
    suspend fun getUserPlants(): UserPlantsResponse

    @POST("user-plants")
    suspend fun addPlant(
        @Body addPlantRequest: AddPlantRequest
    ): AddPlantResponse

    @DELETE("user-plants")
    suspend fun deletePlant(
        @Body deletePlantRequest: DeletePlantRequest
    ): DeletePlantResponse

    @GET("diagnosis/history")
    suspend fun getDiagnosisHistory(): DiagnosisHistoryResponse

}

