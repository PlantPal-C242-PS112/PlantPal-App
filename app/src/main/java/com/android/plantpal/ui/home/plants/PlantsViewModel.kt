package com.android.plantpal.ui.home.plants

import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository

class PlantsViewModel (private val repository: Repository) : ViewModel() {
    fun getAllPlants() = repository.getAllPlants()
}