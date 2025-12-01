package com.autoservice.mobile.data.remote.dto

import com.autoservice.mobile.domain.model.Car
import com.google.gson.annotations.SerializedName

data class CarDto(
    @SerializedName("id") val id: Int,
    @SerializedName("brand") val brand: String,
    @SerializedName("model") val model: String,
    @SerializedName("year") val year: Int,
    @SerializedName("vin") val vin: String?,
    @SerializedName("license_plate") val licensePlate: String?,
    @SerializedName("owner_id") val ownerId: Int
) {
    fun toDomain(): Car = Car(
        id = id,
        brand = brand,
        model = model,
        year = year,
        vin = vin,
        licensePlate = licensePlate,
        ownerId = ownerId
    )
}
