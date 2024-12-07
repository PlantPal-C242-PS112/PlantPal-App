package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class CreateCommentResponse(

	@field:SerializedName("data")
	val data: CommentData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class CommentData(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("user")
	val user: UserCommentCreate,

	@field:SerializedName("content")
	val content: String
)

data class UserCommentCreate(

	@field:SerializedName("profile_photo")
	val profilePhoto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("username")
	val username: String
)
