package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiagnosisHistoryResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("data")
    val data: List<DiagnosisItem>
)

data class DiagnosisItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("result")
    val result: DiagnosisResult
)

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
)