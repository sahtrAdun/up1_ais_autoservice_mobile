package com.autoservice.mobile.domain.usecase.requests

import com.autoservice.mobile.domain.repository.ServiceRequestRepository
import javax.inject.Inject

class RefreshUserRequestsUseCase @Inject constructor(
    private val serviceRequestRepository: ServiceRequestRepository
) {
    suspend operator fun invoke(userId: Int) {
        if (userId > 0) {
            serviceRequestRepository.refreshUserRequests(userId)
        }
    }
}
