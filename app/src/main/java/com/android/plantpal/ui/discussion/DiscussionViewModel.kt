package com.android.plantpal.ui.discussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.plantpal.data.Repository
import com.android.plantpal.data.remote.response.ListItemDiscussions

class DiscussionViewModel(private val repository: Repository) : ViewModel() {
    var selectedPlant: String? = ""

    val discussions: LiveData<PagingData<ListItemDiscussions>> =
        repository.getAllDiscussions().cachedIn(viewModelScope)

    fun getDetailDiscussion(id: Int) = repository.getDetailDiscussion(id)

    fun deleteDiscussion(id: Int) = repository.deleteDiscussion(id)

    fun getComments(id: Int) = repository.getComments(id)

    fun addComment(id: Int, content: String) = repository.addComment(id, content)

    fun likeOrDislike(id: Int) = repository.likeOrDislike(id)

    fun searchDiscussions(query: String): LiveData<PagingData<ListItemDiscussions>> {
        return repository.searchDiscussions(query)
    }

    fun filterDiscussionsByPlantId(plant: String): LiveData<PagingData<ListItemDiscussions>> {
        return repository.filterDiscussionsByPlantId(plant)
    }

    fun getAllPlants() = repository.getAllPlants()

}