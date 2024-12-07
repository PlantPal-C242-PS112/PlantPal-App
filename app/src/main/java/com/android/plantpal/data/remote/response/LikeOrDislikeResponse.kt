package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class LikeOrDislikeResponse(

	@field:SerializedName("data")
	val data: DataLike,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DataLike(

	@field:SerializedName("total_likes")
	val totalLikes: Int
)
