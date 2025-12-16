package com.example.namatara.data.model

data class LoginResponse(
    val token: String,
    val userId: Int,
    val fullName: String,
    val message: String?
)