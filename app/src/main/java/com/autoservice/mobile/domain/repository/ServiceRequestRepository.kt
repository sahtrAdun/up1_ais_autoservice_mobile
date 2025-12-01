package com.autoservice.mobile.domain.repository

import com.autoservice.mobile.domain.model.ServiceRequest
import kotlinx.coroutines.flow.Flow

interface ServiceRequestRepository {
    val userRequests: Flow<List<ServiceRequest>>
    suspend fun getUserRequests(userId: Int): Result<List<ServiceRequest>>
    suspend fun createRequest(request: ServiceRequest): Result<Boolean>
    suspend fun cancelRequest(requestId: Int): Result<Boolean>
    suspend fun refreshUserRequests(userId: Int)
}
