package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetPlantsResponse(

	@field:SerializedName("data")
	val data: List<DataPlantItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DataPlantItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
