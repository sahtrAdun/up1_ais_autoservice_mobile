package com.autoservice.mobile.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.autoservice.mobile.core.ui.BaseViewModel
import com.autoservice.mobile.domain.usecase.auth.GetCurrentUserUseCase
import com.autoservice.mobile.domain.usecase.requests.LoadUserRequestsUseCase
import com.autoservice.mobile.ui.screen.requests.RequestsState
import kotlinx.coroutines.launch

class RequestsViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val loadUserRequestsUseCase: LoadUserRequestsUseCase
) : BaseViewModel<RequestsState>(initial = RequestsState()) {
    init {
        viewModelScope.launch {
            val user = getCurrentUserUseCase.invoke()
            if (user?.id != null) {
                update { it.copy(userId = user.id) }
            }

            loadRequests()
        }
    }

    fun loadRequests() = request {
        val userId = state.value.userId ?: run {
            update { it.copy(error = "Пользователь не авторизован") }
            return@request
        }
        val result = loadUserRequestsUseCase.invoke(userId)

        when {
            result.isSuccess -> {
                val requests = result.getOrNull() ?: return@request
                update { it.copy(requests = requests) }
            }
            else -> update { it.copy(error = "Не удалось получить заявки") }
        }
    }
}