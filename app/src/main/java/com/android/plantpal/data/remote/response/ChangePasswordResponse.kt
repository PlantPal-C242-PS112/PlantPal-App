package com.android.plantpal.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChangeForgotPasswordResponse (
    @field:SerializedName("data")
    val data: ChangeForgotPasswordData,

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String

)

data class ChangeForgotPasswordData(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,
)
