package com.autoservice.mobile.ui.screen.newrequest

import com.autoservice.mobile.core.ui.ViewState
import com.autoservice.mobile.domain.model.Car

data class NewRequestState(
    val userId: Int? = null,
    val selectedCarId: Int? = null,
    val cars: List<Car> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) : ViewState {
    override fun copyWithLoading(isLoading: Boolean): ViewState {
        return this.copy(isLoading = isLoading)
    }
}