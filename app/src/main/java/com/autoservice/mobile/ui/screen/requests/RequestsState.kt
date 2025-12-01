package com.autoservice.mobile.ui.screen.requests

import androidx.compose.runtime.Immutable
import com.autoservice.mobile.core.ui.ViewState
import com.autoservice.mobile.domain.model.ServiceRequest

@Immutable
data class RequestsState(
    val requests: List<ServiceRequest> = emptyList(),
    val userId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState {
    override fun copyWithLoading(isLoading: Boolean): ViewState {
        return this.copy(isLoading = isLoading)
    }
}