package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

class DetailPlantsResponse (
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: DetailPlantData
)

data class DetailPlantData(
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("plant_media")
    val plantMedia: List<PlantMedia>
)

data class PlantMedia(
    @SerializedName("type")
    val type: String,

    @SerializedName("url")
    val url: String

)

