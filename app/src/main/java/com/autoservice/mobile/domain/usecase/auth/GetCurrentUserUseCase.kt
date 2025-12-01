package com.autoservice.mobile.domain.usecase.auth

import com.autoservice.mobile.domain.model.User
import com.autoservice.mobile.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}
