package com.autoservice.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoservice.mobile.domain.usecase.auth.GetCurrentUserUseCase
import com.autoservice.mobile.domain.usecase.auth.IsUserLoggedInUseCase
import com.autoservice.mobile.domain.usecase.auth.LoginUseCase
import com.autoservice.mobile.domain.usecase.auth.LogoutUseCase
import com.autoservice.mobile.ui.screen.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = loginUseCase(username, password)
            _authState.value = if (result.isSuccess) {
                AuthState.Authenticated(result.getOrNull()!!)
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка авторизации")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Unauthenticated
            logoutUseCase.invoke()
            checkAuthStatus()
        }
    }

    private fun checkAuthStatus() = viewModelScope.launch {
        val user = getCurrentUserUseCase()
        _authState.value = if (user != null) {
            AuthState.Authenticated(user)
        } else {
            AuthState.Unauthenticated
        }
    }
}
