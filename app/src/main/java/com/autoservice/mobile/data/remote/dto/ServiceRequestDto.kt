package com.autoservice.mobile.data.remote.dto

import com.autoservice.mobile.domain.model.ServiceRequest
import com.google.gson.annotations.SerializedName

data class ServiceRequestDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("car_id") val carId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val createdDate: String,
    @SerializedName("scheduled_date") val scheduledDate: String?,
    @SerializedName("estimated_cost") val estimatedCost: Double,
    @SerializedName("actual_cost") val actualCost: Double,
    @SerializedName("admin_notes") val adminNotes: String?
) {
    fun toDomain(): ServiceRequest = ServiceRequest(
        id = id,
        carId = carId,
        description = description,
        status = status,
        createdDate = createdDate,
        scheduledDate = scheduledDate,
        estimatedCost = estimatedCost,
        actualCost = actualCost,
        adminNotes = adminNotes
    )
}
