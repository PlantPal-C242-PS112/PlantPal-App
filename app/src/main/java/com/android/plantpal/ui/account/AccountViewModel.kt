package com.android.plantpal.ui.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.plantpal.data.Repository
import kotlinx.coroutines.launch
import java.io.File

class AccountViewModel(private val repository: Repository) : ViewModel() {

    var currentImageUri: Uri? = null
    var fullname: String? = null
    fun getUserDetails() = repository.getUserDetails()

    fun updateProfile(profilePicture: File?, fullname: String) = repository.updateProfile(profilePicture, fullname)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}