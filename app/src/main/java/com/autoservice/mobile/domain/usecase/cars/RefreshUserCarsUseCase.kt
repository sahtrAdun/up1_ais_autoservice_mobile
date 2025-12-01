package com.autoservice.mobile.domain.usecase.cars

import com.autoservice.mobile.domain.repository.CarRepository
import javax.inject.Inject

class RefreshUserCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(userId: Int) {
        if (userId > 0) {
            carRepository.refreshUserCars(userId)
        }
    }
}
