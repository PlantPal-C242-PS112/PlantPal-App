package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiseaseResponse(

	@field:SerializedName("data")
	val data: List<ListItemDisease>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class UPlant(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class ListItemDisease(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("plant")
	val plant: UPlant,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
