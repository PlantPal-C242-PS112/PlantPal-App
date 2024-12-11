package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailDiscussionResponse(

	@field:SerializedName("data")
	val data: DetailDiscussionData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DetailDiscussionData(

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("plant")
	val plant: PlantDetail,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("total_likes")
	val totalLikes: Int,

	@field:SerializedName("media_url")
	val mediaUrl: String,

	@field:SerializedName("user")
	val user: UserDetail,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("is_liked")
	val isLiked: Boolean
)

data class UserDetail(

	@field:SerializedName("profile_photo")
	val profilePhoto: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("username")
	val username: String
)

data class PlantDetail(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
