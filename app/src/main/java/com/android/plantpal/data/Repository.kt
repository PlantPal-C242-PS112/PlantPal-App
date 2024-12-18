package com.android.plantpal.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.android.plantpal.data.database.DiscussionRoomDatabase
import com.android.plantpal.data.preference.UserModel
import com.android.plantpal.data.preference.UserPreference
import com.android.plantpal.data.remote.ChangeForgotPasswordRequest
import com.android.plantpal.data.remote.CommentRequest
import com.android.plantpal.data.remote.LoginRequest
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.data.remote.SendOtpRequest
import com.android.plantpal.data.remote.VerifyOtpRequest
import com.android.plantpal.data.remote.response.AddPlantResponse
import com.android.plantpal.data.remote.response.ChangeForgotPasswordResponse
import com.android.plantpal.data.remote.response.CreateCommentResponse
import com.android.plantpal.data.remote.response.CreateDiscussionResponse
import com.android.plantpal.data.remote.response.CultivationData
import com.android.plantpal.data.remote.response.DeleteDiscussionResponse
import com.android.plantpal.data.remote.response.DetailDiscussionData
import com.android.plantpal.data.remote.response.DetailDiseaseData
import com.android.plantpal.data.remote.response.DetailPlantData
import com.android.plantpal.data.remote.response.DiagnosisItem
import com.android.plantpal.data.remote.response.DiagnosisResponse
import com.android.plantpal.data.remote.response.LikeOrDislikeResponse
import com.android.plantpal.data.remote.response.ListItemComment
import com.android.plantpal.data.remote.response.ListItemDiscussions
import com.android.plantpal.data.remote.response.ListItemDisease
import com.android.plantpal.data.remote.response.ListItemPlant
import com.android.plantpal.data.remote.response.ListPlantDisease
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.data.remote.response.RegisterResponse
import com.android.plantpal.data.remote.response.SendOtpResponse
import com.android.plantpal.data.remote.response.UpdateProfileResponse
import com.android.plantpal.data.remote.response.UserDetailResponse
import com.android.plantpal.data.remote.response.UserPlant
import com.android.plantpal.data.remote.response.VerifyOtpResponse
import com.android.plantpal.data.remote.retrofit.ApiService
import com.android.plantpal.ui.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Repository (
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val database: DiscussionRoomDatabase
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

    fun getUserDetails(): LiveData<Result<UserDetailResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getUserDetails()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getAllPlants(): LiveData<Result<List<ListItemPlant>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getAllPlants()
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getAllDiseases(): LiveData<Result<List<ListItemDisease>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getAllDiseases()
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getAllDiscussions(): LiveData<PagingData<ListItemDiscussions>>
    {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = DiscussionRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.discussionDao().getAllDiscussion()
            }
        ).liveData
    }

    fun getDetailDiscussion(id: Int): LiveData<Result<DetailDiscussionData>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailDiscussion(id)
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getDetailDisease(id: Int): LiveData<Result<DetailDiseaseData>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailDisease(id)
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getDetailPlants(id: Int): LiveData<Result<DetailPlantData>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailPlants(id)
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getCultivationTips(id: Int): LiveData<Result<CultivationData>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getCultivationTips(id)
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun addDiscussion(
        title: String,
        content: String,
        media: File,
        plant_id: Int
    ): LiveData<Result<CreateDiscussionResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val requestImageFile = media.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "media",
                media.name,
                requestImageFile
            )
            val titleBody = title.toRequestBody("text/plain".toMediaType())
            val contentBody = content.toRequestBody("text/plain".toMediaType())
            val plantIdBody = plant_id.toString().toRequestBody("text/plain".toMediaType())
            val response = apiService.addDiscussions(titleBody, contentBody, multipartBody, plantIdBody)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun deleteDiscussion(id: Int): LiveData<Result<DeleteDiscussionResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try{
            val response = apiService.deleteDiscussion(id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("ErrorDel: ${e.message}"))
        }
    }

    fun getComments(id: Int): LiveData<Result<List<ListItemComment>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try{
            val response = apiService.getComments(id).data
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("ErrorDel: ${e.message}"))
        }
    }

    fun addComment(id: Int, comment: String): LiveData<Result<CreateCommentResponse>> = liveData (Dispatchers.IO){
        emit(Result.Loading)
        try{
            val comContent = CommentRequest(
                content = comment
            )
            val response = apiService.addComments(id, comContent)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("ErrorDel: ${e.message}"))
        }
    }

    fun updateProfile(
        profile_picture: File?,
        fullname: String
    ): LiveData<Result<UpdateProfileResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val requestImageFile = profile_picture?.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = requestImageFile?.let {
                MultipartBody.Part.createFormData(
                    "profile_picture",
                    profile_picture.name,
                    it
                )
            }
            val titleBody = fullname.toRequestBody("text/plain".toMediaType())
            Log.d("UpdateProfile", "$multipartBody, $titleBody")
            val response = apiService.updateProfile(multipartBody, titleBody)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("UpdateProfile", "Error: ${e.message}")
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun likeOrDislike(id: Int): LiveData<Result<LikeOrDislikeResponse>> = liveData (Dispatchers.IO){
        emit(Result.Loading)
        try{
            val response = apiService.likeOrDislike(id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("ErrorDel: ${e.message}"))
        }
    }

    fun getDiagnosis(
        image: File
    ): LiveData<Result<DiagnosisResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = requestImageFile.let {
                MultipartBody.Part.createFormData(
                    "image",
                    image.name,
                    it
                )
            }
            val response = apiService.getDiagnosis(multipartBody)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getUserPlants(): LiveData<Result<List<UserPlant>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getUserPlants()
            if (response.status) {
                emit(Result.Success(response.data))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun addPlant(plantId: Int): LiveData<Result<AddPlantResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.addPlant(plantId)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun deletePlant(plantId: Int): LiveData<Result<Boolean>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try{
            apiService.deletePlant(plantId)
            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error("ErrorDel: ${e.message}"))
        }
    }

    fun getDiagnosisHistory(): LiveData<Result<List<DiagnosisItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getDiagnosisHistory()
            if (response.status) {
                emit(Result.Success(response.data))
            } else {
                emit(Result.Error("Error: Unable to fetch data"))
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun deleteHistory(id: String): LiveData<Result<Boolean>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            apiService.deleteHistory(id)
            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error("ErrorDel: ${e.message}"))
        }
    }

    fun deleteAllHistory(): LiveData<Result<Boolean>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            apiService.deleteAllHistory()
            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error("ErrorDel: ${e.message}"))
        }
    }

    fun getPlantDiseases(id: Int): LiveData<Result<List<ListPlantDisease>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getPlantDiseases(id)
            emit(Result.Success(response.data))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun searchDiscussions(query: String): LiveData<PagingData<ListItemDiscussions>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { database.discussionDao().searchByTitleOrContent(query) }
        ).liveData
    }

    fun filterDiscussionsByPlantId(plant: String): LiveData<PagingData<ListItemDiscussions>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { database.discussionDao().filterByPlantId(plant) }
        ).liveData
    }


    companion object {
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService,
            database: DiscussionRoomDatabase
        ) = Repository(preference, apiService, database)
    }
}