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
