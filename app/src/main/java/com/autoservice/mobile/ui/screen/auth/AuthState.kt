package com.autoservice.mobile.ui.screen.auth

import com.autoservice.mobile.domain.model.User

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}