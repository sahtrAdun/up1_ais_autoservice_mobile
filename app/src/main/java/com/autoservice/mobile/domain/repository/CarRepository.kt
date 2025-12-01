package com.autoservice.mobile.domain.repository

import com.autoservice.mobile.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    val userCars: Flow<List<Car>>
    suspend fun getUserCars(userId: Int): Result<List<Car>>
    suspend fun addCar(car: Car): Result<Boolean>
    suspend fun refreshUserCars(userId: Int)
}
