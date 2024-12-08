package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddPlantResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: AddPlantData
)

data class AddPlantData(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("plant_id")
    val plantId: Int,

    @SerializedName("sowing_date")
    val sowingDate: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)
