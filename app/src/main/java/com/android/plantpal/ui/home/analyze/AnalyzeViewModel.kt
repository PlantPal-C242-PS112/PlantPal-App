package com.android.plantpal.ui.home.analyze

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import java.io.File

class AnalyzeViewModel (private val repository: Repository) : ViewModel() {
    var currentImageUri: Uri? = null

    fun getDiagnose(image: File) = repository.getDiagnosis(image)
}