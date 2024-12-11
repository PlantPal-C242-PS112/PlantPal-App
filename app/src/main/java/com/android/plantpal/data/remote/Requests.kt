package com.android.plantpal.data.remote
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullname: String
)

data class LoginRequest(
    val identifier: String,
    val password: String,
)

data class CommentRequest(
    val content: String
)
data class SendOtpRequest(
    val email: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class ChangeForgotPasswordRequest(
    val email: String,
    val password: String
)





