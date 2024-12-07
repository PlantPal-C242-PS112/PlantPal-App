package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class CreateDiscussionResponse(

	@field:SerializedName("data")
	val data: CreateDiscussionData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class PlantUp(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class CreateDiscussionData(

	@field:SerializedName("plant_id")
	val plantId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("plant")
	val plant: PlantUp,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("total_likes")
	val totalLikes: Int,

	@field:SerializedName("media_url")
	val mediaUrl: String,

	@field:SerializedName("user")
	val user: UserUp,

	@field:SerializedName("content")
	val content: String
)

data class UserUp(

	@field:SerializedName("profile_photo")
	val profilePhoto: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("username")
	val username: String
)
