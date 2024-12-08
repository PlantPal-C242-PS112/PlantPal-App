package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class CultivationTipsResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: CultivationData
)

data class CultivationData(
    @SerializedName("name")
    val name: String,

    @SerializedName("cultivation_tips")
    val cultivationTips: String,

    @SerializedName("plant_media")
    val plantMedia: List<CultivationPlantMedia>
)

data class CultivationPlantMedia(
    @SerializedName("type")
    val type: String,

    @SerializedName("url")
    val url: String
)
