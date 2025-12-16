package com.example.namatara.data.model

data class RegisterRequest(
    val username: String,
    val fullName: String,
    val dateOfBirth: String,
    val password: String
)