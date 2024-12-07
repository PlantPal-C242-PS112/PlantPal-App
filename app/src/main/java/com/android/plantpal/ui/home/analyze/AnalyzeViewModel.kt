package com.android.plantpal.ui.home.analyze

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository

class AnalyzeViewModel (private val repository: Repository) : ViewModel() {
    var currentImageUri: Uri? = null
}