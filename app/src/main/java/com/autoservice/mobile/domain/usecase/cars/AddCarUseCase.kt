package com.autoservice.mobile.domain.usecase.cars


import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.domain.repository.CarRepository
import com.autoservice.mobile.domain.util.ValidationUtil
import javax.inject.Inject

class AddCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(car: Car): Result<Boolean> {
        // Валидация данных
        if (car.brand.isBlank()) {
            return Result.failure(IllegalArgumentException("Марка автомобиля не может быть пустой"))
        }
        if (car.model.isBlank()) {
            return Result.failure(IllegalArgumentException("Модель автомобиля не может быть пустой"))
        }
        if (!ValidationUtil.isValidYear(car.year)) {
            return Result.failure(IllegalArgumentException("Некорректный год выпуска"))
        }
        if (car.vin != null && !ValidationUtil.isValidVIN(car.vin)) {
            return Result.failure(IllegalArgumentException("Некорректный формат VIN"))
        }

        return carRepository.addCar(car)
    }
}

