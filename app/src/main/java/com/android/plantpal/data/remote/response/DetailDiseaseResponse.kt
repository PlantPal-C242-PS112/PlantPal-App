package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailDiseaseResponse(

	@field:SerializedName("data")
	val data: DetailDiseaseData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class MedicinesItem(

	@field:SerializedName("medicine_links")
	val medicineLinks: List<MedicineLinksItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String
)

data class PlantDis(

	@field:SerializedName("name")
	val name: String
)

data class DetailDiseaseData(

	@field:SerializedName("treatment")
	val treatment: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("medicines")
	val medicines: List<MedicinesItem>,

	@field:SerializedName("disease_media")
	val diseaseMedia: List<DiseaseMediaItem>,

	@field:SerializedName("plant")
	val plant: PlantDis,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("prevention")
	val prevention: String
)

data class DiseaseMediaItem(

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("url")
	val url: String
)

data class MedicineLinksItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("url")
	val url: String
)
