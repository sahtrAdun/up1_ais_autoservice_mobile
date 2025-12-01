package com.autoservice.mobile.domain.usecase.auth

import com.autoservice.mobile.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.getCurrentUser() != null
    }
}
