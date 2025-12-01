package com.autoservice.mobile.domain.repository

import com.autoservice.mobile.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun getCurrentUser(): User?

    suspend fun logout(): Result<Unit>

    val currentUser: StateFlow<User?>
}
