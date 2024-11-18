package com.android.plantpal.data

import com.android.plantpal.data.preference.UserPreference
import com.android.plantpal.data.remote.retrofit.ApiService

class Repository (
    private val userPreference: UserPreference,
    private val apiService: ApiService
    ) {

    companion object {
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService
        ) = Repository(preference, apiService)
    }
}