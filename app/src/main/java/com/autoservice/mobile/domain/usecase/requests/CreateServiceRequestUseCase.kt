package com.autoservice.mobile.domain.usecase.requests

import com.autoservice.mobile.domain.model.ServiceRequest
import com.autoservice.mobile.domain.repository.ServiceRequestRepository
import javax.inject.Inject

class CreateServiceRequestUseCase @Inject constructor(
    private val serviceRequestRepository: ServiceRequestRepository
) {
    suspend operator fun invoke(request: ServiceRequest): Result<Boolean> {
        // Валидация данных
        if (request.carId <= 0) {
            return Result.failure(IllegalArgumentException("Некорректный ID автомобиля"))
        }
        if (request.description.isBlank()) {
            return Result.failure(IllegalArgumentException("Описание проблемы не может быть пустым"))
        }
        if (request.description.length < 10) {
            return Result.failure(IllegalArgumentException("Описание должно содержать не менее 10 символов"))
        }

        return serviceRequestRepository.createRequest(request)
    }
}
