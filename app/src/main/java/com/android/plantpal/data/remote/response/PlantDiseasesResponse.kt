package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class PlantDiseasesResponse(

	@field:SerializedName("data")
	val data: List<ListPlantDisease>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class ListPlantDisease(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name")
	val name: String
)
