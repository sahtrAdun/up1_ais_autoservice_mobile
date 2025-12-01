package com.autoservice.mobile.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.autoservice.mobile.core.ui.BaseViewModel
import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.domain.usecase.auth.GetCurrentUserUseCase
import com.autoservice.mobile.domain.usecase.cars.AddCarUseCase
import com.autoservice.mobile.domain.usecase.cars.GetUserCarsUseCase
import com.autoservice.mobile.domain.usecase.cars.LoadUserCarsUseCase
import com.autoservice.mobile.ui.screen.cars.CarsState
import kotlinx.coroutines.launch

class CarsViewModel(
    private val getUserCarsUseCase: GetUserCarsUseCase,
    private val loadUserCarsUseCase: LoadUserCarsUseCase,
    private val addCarUseCase: AddCarUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : BaseViewModel<CarsState>(initial = CarsState()) {
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

    fun addCar(car: Car) {
        viewModelScope.launch {
            val result = addCarUseCase(car)
            if (result.isSuccess) {
                loadCars(car.ownerId)
            } else {
                update { it.copy(error = "Не удалось добавить автомобиль") }
            }
        }
    }

    private fun loadCars() {
        viewModelScope.launch {
            getUserCarsUseCase().collect { cars ->
                update { it.copy(cars = cars) }
            }
        }
    }
}