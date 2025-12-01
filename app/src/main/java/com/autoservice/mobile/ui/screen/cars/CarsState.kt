package com.autoservice.mobile.ui.screen.cars

import androidx.compose.runtime.Immutable
import com.autoservice.mobile.core.ui.ViewState
import com.autoservice.mobile.domain.model.Car

@Immutable
data class CarsState(
    val cars: List<Car> = emptyList(),
    val userId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState {
    override fun copyWithLoading(isLoading: Boolean): ViewState {
        return this.copy(isLoading = isLoading)
    }
}