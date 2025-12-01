package com.autoservice.mobile.domain.model

data class ServiceRequest(
    val id: Int?,
    val carId: Int,
    val description: String,
    val status: String,
    val createdDate: String,
    val scheduledDate: String?,
    val estimatedCost: Double,
    val actualCost: Double,
    val adminNotes: String?
)
