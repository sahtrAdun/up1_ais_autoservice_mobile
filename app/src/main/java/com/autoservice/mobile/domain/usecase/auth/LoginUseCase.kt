package com.autoservice.mobile.domain.usecase.auth

import com.autoservice.mobile.domain.model.User
import com.autoservice.mobile.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Логин и пароль не могут быть пустыми"))
        }
        return authRepository.login(username, password)
    }
}
