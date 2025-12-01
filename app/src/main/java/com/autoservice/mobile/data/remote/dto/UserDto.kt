package com.autoservice.mobile.data.remote.dto

import com.autoservice.mobile.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?
) {
    fun toDomain(): User = User(
        id = id,
        username = username,
        email = email,
        phone = phone,
        role = role
    )
}
