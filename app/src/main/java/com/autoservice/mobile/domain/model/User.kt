package com.autoservice.mobile.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String?,
    val phone: String?,
    val role: String
)
