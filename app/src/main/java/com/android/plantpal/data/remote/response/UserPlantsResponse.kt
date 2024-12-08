package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserPlantsResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<UserPlant>
)

data class UserPlant(
    @SerializedName("plant_id")
    val plantId: Int,
    @SerializedName("sowing_date")
    val sowingDate: String?,
    @SerializedName("plant")
    val plant: UserPlantDetail,
    @SerializedName("created_at")
    val createdAt: String
)

data class UserPlantDetail(
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String
)
