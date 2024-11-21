package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: RegisterData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class RegisterData(

	@field:SerializedName("otp_expiry")
	val otpExpiry: Any,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email_verified")
	val emailVerified: Boolean,

	@field:SerializedName("profile_photo")
	val profilePhoto: Any,

	@field:SerializedName("otp")
	val otp: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("forgot_password_verified")
	val forgotPasswordVerified: Boolean,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
