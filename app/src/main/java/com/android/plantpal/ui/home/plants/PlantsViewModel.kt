package com.android.plantpal.ui.home.plants

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.remote.response.CultivationData
import com.android.plantpal.data.remote.response.DetailPlantData
import com.android.plantpal.ui.utils.Result

class PlantsViewModel (private val repository: Repository) : ViewModel() {

    val plantDetailsWithCultivationTips = MediatorLiveData<Pair<Result<DetailPlantData>?, Result<CultivationData>?>>()

    fun setPlantId(id: Int) {
        val detailPlantsLiveData = getDetailPlantsLiveData(id)
        val cultivationTipsLiveData = getCultivationTipsLiveData(id)

        plantDetailsWithCultivationTips.addSource(detailPlantsLiveData) { plantDetailsResult ->
            plantDetailsWithCultivationTips.value = Pair(plantDetailsResult, plantDetailsWithCultivationTips.value?.second)
        }
        plantDetailsWithCultivationTips.addSource(cultivationTipsLiveData) { cultivationTipsResult ->
            plantDetailsWithCultivationTips.value = Pair(plantDetailsWithCultivationTips.value?.first, cultivationTipsResult)
        }
    }

    fun getDetailPlantsLiveData(id: Int): LiveData<Result<DetailPlantData>> {
        return repository.getDetailPlants(id)
    }

    fun getCultivationTipsLiveData(id: Int): LiveData<Result<CultivationData>> {
        return repository.getCultivationTips(id)
    }


    fun getAllPlants() = repository.getAllPlants()

    fun getDetailPlants(id: Int) = repository.getDetailPlants(id)

    fun getCultivationTips(id: Int) = repository.getCultivationTips(id)

    fun addPlant(plantId: Int) = repository.addPlant(plantId)

    fun deletePlant(plantId: Int) = repository.deletePlant(plantId)

    fun getUserPlant() = repository.getUserPlants()


}