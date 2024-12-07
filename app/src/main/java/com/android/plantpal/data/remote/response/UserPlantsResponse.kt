package com.android.plantpal.data.remote.response

data class UserPlantsResponse(
    val status: Boolean,
    val message: String,
    val data: List<UserPlant>
)

data class UserPlant(
    val plant_id: Int,
    val sowing_date: String?,
    val plant: PlantDetail,
    val created_at: String
)

data class PlantDetail(
    val name: String,
    val icon: String
)
