package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteDiscussionResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)
