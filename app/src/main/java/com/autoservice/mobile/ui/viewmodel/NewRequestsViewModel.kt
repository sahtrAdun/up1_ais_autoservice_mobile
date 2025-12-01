package com.autoservice.mobile.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.autoservice.mobile.core.ui.BaseViewModel
import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.domain.model.ServiceRequest
import com.autoservice.mobile.domain.usecase.auth.GetCurrentUserUseCase
import com.autoservice.mobile.domain.usecase.cars.GetUserCarsUseCase
import com.autoservice.mobile.domain.usecase.cars.LoadUserCarsUseCase
import com.autoservice.mobile.domain.usecase.requests.CreateServiceRequestUseCase
import com.autoservice.mobile.ui.screen.newrequest.NewRequestState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewRequestsViewModel(
    private val getUserCarsUseCase: GetUserCarsUseCase,
    private val loadUserCarsUseCase: LoadUserCarsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val createServiceRequestUseCase: CreateServiceRequestUseCase
) : BaseViewModel<NewRequestState>(initial = NewRequestState()) {
    init {
        viewModelScope.launch {
            val user = getCurrentUserUseCase.invoke()
            if (user?.id != null) {
                update { it.copy(userId = user.id) }
            }

            loadCars()
        }
    }

    fun loadCars(userId: Int? = state.value.userId) {
        if (userId == null) {
            return
        }

        request {
            val result = loadUserCarsUseCase(userId)
            if (result.isSuccess) {
                update { it.copy(cars = result.getOrNull() ?: emptyList()) }
            } else {
                update { it.copy(error = "Не удалось загрузить автомобили") }
            }
        }
    }

    fun createApplication(description: String) = request {
        if (state.value.selectedCarId == null) return@request

        val request = ServiceRequest(
            id = 0,
            carId = state.value.selectedCarId!!,
            description = description,
            status = "PENDING",
            createdDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
            scheduledDate = null,
            estimatedCost = 0.0,
            actualCost = 0.0,
            adminNotes = null
        )
        val result = createServiceRequestUseCase.invoke(request)

        if (result.isSuccess) {
            update { it.copy(isSuccess = true) }
        } else {
            update { it.copy(error = "Не удалось создать заявку") }
        }
    }

    fun selectCar(car: Car) {
        update { it.copy(selectedCarId = car.id) }
    }

    fun clearSuccess() {
        update { it.copy(
            isSuccess = false,
            selectedCarId = null,
            error = null
        ) }
    }
}