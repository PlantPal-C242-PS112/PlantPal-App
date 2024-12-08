package com.android.plantpal.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiagnosisHistoryResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("data")
    val data: List<DiagnosisItem>
): Parcelable

@Parcelize
data class DiagnosisItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("result")
    val result: DiagnosisResult
): Parcelable

@Parcelize
data class DiagnosisResult(
    @SerializedName("message")
    val message: String,
    @SerializedName("class_name")
    val className: String,
    @SerializedName("plant_id")
    val plantId: Int,
    @SerializedName("disease_id")
    val diseaseId: Int,
    @SerializedName("confidence_score")
    val confidenceScore: Double,
    @SerializedName("image_url")
    val imageUrl: String?
): Parcelable