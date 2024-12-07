package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class SendOtpResponse (
    @field:SerializedName("data")
    val data: SendOtpData,

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String

)

data class SendOtpData(
    @field:SerializedName("email")
    val email: String
)
