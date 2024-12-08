package com.android.plantpal.ui.home.disease

import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository

class DiseaseViewModel (private val repository: Repository) : ViewModel() {
    fun getAllDisease() = repository.getAllDiseases()

    fun getDetailDisease(id: Int) = repository.getDetailDisease(id)
}