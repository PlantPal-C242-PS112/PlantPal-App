package com.android.plantpal.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPlantsResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<UserPlant>
): Parcelable

@Parcelize
data class UserPlant(
    @SerializedName("plant_id")
    val plantId: Int,
    @SerializedName("sowing_date")
    val sowingDate: String?,
    @SerializedName("plant")
    val plant: UserPlantDetail,
    @SerializedName("created_at")
    val createdAt: String
): Parcelable

@Parcelize
data class UserPlantDetail(
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String
): Parcelable
