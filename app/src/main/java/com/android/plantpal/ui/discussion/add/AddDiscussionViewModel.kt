package com.android.plantpal.ui.discussion.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import java.io.File

class AddDiscussionViewModel (private val repository: Repository) : ViewModel() {
    var currentImageUri: Uri? = null
    var selectedPlant: Int? = null

    fun getAllPlants() = repository.getAllPlants()

    fun addDiscussion(title: String, content: String, media: File, plant_id: Int) =
        repository.addDiscussion(title, content, media, plant_id)

}