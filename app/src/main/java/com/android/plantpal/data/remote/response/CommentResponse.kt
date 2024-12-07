package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class CommentResponse(

	@field:SerializedName("data")
	val data: List<ListItemComment>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class UserComment(

	@field:SerializedName("profile_photo")
	val profilePhoto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("username")
	val username: String
)

data class ListItemComment(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("user")
	val user: UserComment,

	@field:SerializedName("content")
	val content: String
)
