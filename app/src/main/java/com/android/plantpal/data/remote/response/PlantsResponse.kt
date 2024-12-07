package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class PlantsResponse(

	@field:SerializedName("data")
	val data: List<ListItemPlant>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class ListItemPlant(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("id")
	val id: Int
)
