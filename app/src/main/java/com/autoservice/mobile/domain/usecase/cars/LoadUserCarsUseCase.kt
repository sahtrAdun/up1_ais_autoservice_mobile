package com.autoservice.mobile.domain.usecase.cars

import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.domain.repository.CarRepository
import javax.inject.Inject

class LoadUserCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<Car>> {
        if (userId <= 0) {
            return Result.failure(IllegalArgumentException("Некорректный ID пользователя"))
        }
        return carRepository.getUserCars(userId)
    }
}
