package com.autoservice.mobile.domain.usecase.cars

import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    operator fun invoke(): Flow<List<Car>> {
        return carRepository.userCars
    }
}
