package com.autoservice.mobile.domain.model

data class Car(
    val id: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val vin: String?,
    val licensePlate: String?,
    val ownerId: Int
)
