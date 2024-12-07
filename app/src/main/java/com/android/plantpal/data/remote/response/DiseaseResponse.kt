package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiseaseResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DataItem(

	@field:SerializedName("disease_media")
	val diseaseMedia: List<DiseaseMediaItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class DiseaseMediaItem(

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("url")
	val url: String
)
