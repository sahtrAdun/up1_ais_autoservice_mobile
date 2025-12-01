package com.autoservice.mobile.data.repository

import com.autoservice.mobile.data.remote.api.SupabaseApi
import com.autoservice.mobile.domain.model.ServiceRequest
import com.autoservice.mobile.domain.repository.ServiceRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ServiceRequestRepositoryImpl(
    private val supabaseApi: SupabaseApi
) : ServiceRequestRepository {

    private val _userRequests = MutableStateFlow<List<ServiceRequest>>(emptyList())
    override val userRequests: StateFlow<List<ServiceRequest>> = _userRequests.asStateFlow()

    override suspend fun getUserRequests(userId: Int): Result<List<ServiceRequest>> {
        return try {
            val carsResponse = supabaseApi.getCarsByOwner("eq.$userId")

            if (!carsResponse.isSuccessful || carsResponse.body() == null) {
                return Result.failure(Exception("Ошибка загрузки автомобилей"))
            }

            val userCarIds = carsResponse.body()!!.map { it.id }
            val allRequests = mutableListOf<ServiceRequest>()

            for (carId in userCarIds) {
                val requestsResponse = supabaseApi.getRequestsByCar("eq.$carId")
                if (requestsResponse.isSuccessful && requestsResponse.body() != null) {
                    val carRequests = requestsResponse.body()!!.map { it.toDomain() }
                    allRequests.addAll(carRequests)
                }
            }

            _userRequests.value = allRequests
            Result.success(allRequests)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRequest(request: ServiceRequest): Result<Boolean> {
        return try {
            val requestDto = com.autoservice.mobile.data.remote.dto.ServiceRequestDto(
                id = 0,
                carId = request.carId,
                description = request.description,
                status = request.status,
                createdDate = request.createdDate,
                scheduledDate = request.scheduledDate,
                estimatedCost = request.estimatedCost,
                actualCost = request.actualCost,
                adminNotes = request.adminNotes
            )

            val response = supabaseApi.createServiceRequest(requestDto)

            if (response.isSuccessful && response.body() != null) {
                val createdRequest = response.body()!![0].toDomain()
                _userRequests.update { currentRequests ->
                    currentRequests + createdRequest
                }
                Result.success(true)
            } else {
                Result.failure(Exception("Ошибка создания заявки: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelRequest(requestId: Int): Result<Boolean> {
        return try {
            val response = supabaseApi.updateRequestStatus(
                id = "eq.$requestId",
                update = mapOf("status" to "CANCELLED")
            )

            if (response.isSuccessful) {
                _userRequests.update { currentRequests ->
                    currentRequests.map { request ->
                        if (request.id == requestId) {
                            request.copy(status = "CANCELLED")
                        } else {
                            request
                        }
                    }
                }
                Result.success(true)
            } else {
                Result.failure(Exception("Ошибка отмены заявки: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshUserRequests(userId: Int) {
        getUserRequests(userId)
    }
}