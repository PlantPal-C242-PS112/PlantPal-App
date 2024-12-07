package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse (
    @field:SerializedName("data")
    val data: VerifyOtpData,

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String

)

data class VerifyOtpData(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("otp")
    val otp: String
)
