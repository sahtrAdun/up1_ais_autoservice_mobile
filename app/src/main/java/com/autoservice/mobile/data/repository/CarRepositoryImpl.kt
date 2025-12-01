package com.autoservice.mobile.data.repository

import com.autoservice.mobile.data.remote.api.SupabaseApi
import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.domain.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarRepositoryImpl(
    private val supabaseApi: SupabaseApi
) : CarRepository {

    private val _userCars = MutableStateFlow<List<Car>>(emptyList())
    override val userCars: StateFlow<List<Car>> = _userCars.asStateFlow()

    override suspend fun getUserCars(userId: Int): Result<List<Car>> {
        return try {
            val response = supabaseApi.getCarsByOwner("eq.$userId")

            if (response.isSuccessful && response.body() != null) {
                val cars = response.body()!!.map { it.toDomain() }
                _userCars.value = cars
                Result.success(cars)
            } else {
                Result.failure(Exception("Ошибка загрузки автомобилей: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addCar(car: Car): Result<Boolean> {
        return try {
            val carDto = com.autoservice.mobile.data.remote.dto.CarDto(
                id = 0,
                brand = car.brand,
                model = car.model,
                year = car.year,
                vin = car.vin,
                licensePlate = car.licensePlate,
                ownerId = car.ownerId
            )

            val response = supabaseApi.createCar(carDto)

            if (response.isSuccessful && response.body() != null) {
                val createdCar = response.body()!![0].toDomain()
                _userCars.update { currentCars ->
                    currentCars + createdCar
                }
                Result.success(true)
            } else {
                Result.failure(Exception("Ошибка создания автомобиля: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshUserCars(userId: Int) {
        getUserCars(userId)
    }
}