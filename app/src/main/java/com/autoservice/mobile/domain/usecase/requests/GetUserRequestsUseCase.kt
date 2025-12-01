package com.autoservice.mobile.domain.usecase.requests

import com.autoservice.mobile.domain.model.ServiceRequest
import com.autoservice.mobile.domain.repository.ServiceRequestRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserRequestsUseCase @Inject constructor(
    private val serviceRequestRepository: ServiceRequestRepository
) {
    operator fun invoke(): Flow<List<ServiceRequest>> {
        return serviceRequestRepository.userRequests
    }
}
