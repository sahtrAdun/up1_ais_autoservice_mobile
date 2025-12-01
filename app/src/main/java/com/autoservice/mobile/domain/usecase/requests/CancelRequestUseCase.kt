package com.autoservice.mobile.domain.usecase.requests

import com.autoservice.mobile.domain.repository.ServiceRequestRepository
import javax.inject.Inject

class CancelRequestUseCase @Inject constructor(
    private val serviceRequestRepository: ServiceRequestRepository
) {
    suspend operator fun invoke(requestId: Int): Result<Boolean> {
        if (requestId <= 0) {
            return Result.failure(IllegalArgumentException("Некорректный ID заявки"))
        }
        return serviceRequestRepository.cancelRequest(requestId)
    }
}
