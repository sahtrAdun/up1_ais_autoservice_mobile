package com.autoservice.mobile.domain.usecase.requests

import com.autoservice.mobile.domain.model.ServiceRequest
import com.autoservice.mobile.domain.repository.ServiceRequestRepository
import javax.inject.Inject

class LoadUserRequestsUseCase @Inject constructor(
    private val serviceRequestRepository: ServiceRequestRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<ServiceRequest>> {
        if (userId <= 0) {
            return Result.failure(IllegalArgumentException("Некорректный ID пользователя"))
        }
        return serviceRequestRepository.getUserRequests(userId)
    }
}
