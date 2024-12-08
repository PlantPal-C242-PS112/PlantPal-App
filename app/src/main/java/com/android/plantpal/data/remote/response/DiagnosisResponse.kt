package com.android.plantpal.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class DiagnosisResponse(

	@field:SerializedName("data")
	val data: DiagData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
): Parcelable

@Parcelize
data class PlantDiag(

	@field:SerializedName("name")
	val name: String
): Parcelable

@Parcelize
data class DiseaseData(

	@field:SerializedName("treatment")
	val treatment: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("medicines")
	val medicines: @RawValue List<Any>,

	@field:SerializedName("disease_media")
	val diseaseMedia: @RawValue List<Any>,

	@field:SerializedName("plant")
	val plant: PlantDiag,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("prevention")
	val prevention: String
): Parcelable

@Parcelize
data class Top3Item(

	@field:SerializedName("plant_id")
	val plantId: Int,

	@field:SerializedName("disease_id")
	val diseaseId: Int,

	@field:SerializedName("confidence_score")
	val confidenceScore: @RawValue Any,

	@field:SerializedName("class_name")
	val className: String
): Parcelable

@Parcelize
data class DiagData(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("diseaseData")
	val diseaseData: DiseaseData,

	@field:SerializedName("top3")
	val top3: List<Top3Item>
): Parcelable
