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
import com.android.plantpal.data.remote.CommentRequest
import com.android.plantpal.data.remote.LoginRequest
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.data.remote.response.CreateCommentResponse
import com.android.plantpal.data.remote.response.CreateDiscussionResponse
import com.android.plantpal.data.remote.response.DeleteDiscussionResponse
import com.android.plantpal.data.remote.response.DetailDiscussionData
import com.android.plantpal.data.remote.response.LikeOrDislikeResponse
import com.android.plantpal.data.remote.response.ListItemComment
import com.android.plantpal.data.remote.response.ListItemDiscussions
import com.android.plantpal.data.remote.response.ListItemPlant
import com.android.plantpal.data.remote.response.LoginResponse
import com.android.plantpal.data.remote.response.RegisterResponse
import com.android.plantpal.data.remote.response.UpdateProfileResponse
import com.android.plantpal.data.remote.response.UserDetailResponse
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
            if (response.data != null) {
                emit(Result.Success(response.data))
            } else {
                emit(Result.Error("Discussion not found"))
            }
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

    companion object {
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService,
            database: DiscussionRoomDatabase
        ) = Repository(preference, apiService, database)
    }
}