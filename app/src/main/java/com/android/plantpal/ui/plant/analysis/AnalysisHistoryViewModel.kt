package com.android.plantpal.ui.plant.analysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.remote.response.DiagnosisItem
import com.android.plantpal.ui.utils.Result


class AnalysisHistoryViewModel(private val repository: Repository) : ViewModel() {

    fun getDiagnosisHistory(): LiveData<Result<List<DiagnosisItem>>> {
        return repository.getDiagnosisHistory()
    }

    fun deleteHistory(id: String) = repository.deleteHistory(id)

    fun deleteAllHistory() = repository.deleteAllHistory()
}