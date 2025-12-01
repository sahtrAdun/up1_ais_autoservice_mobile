// data/remote/api/SupabaseApi.kt
package com.autoservice.mobile.data.remote.api

import com.autoservice.mobile.data.remote.dto.CarDto
import com.autoservice.mobile.data.remote.dto.ServiceRequestDto
import com.autoservice.mobile.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApi {

    // Auth - получаем пользователя по логину
    @GET("users")
    suspend fun getUserByUsername(
        @Query("username") username: String
    ): Response<List<UserDto>>

    // Cars - получаем автомобили пользователя
    @GET("cars")
    suspend fun getCarsByOwner(
        @Query("owner_id") ownerId: String
    ): Response<List<CarDto>>

    // Cars - добавляем автомобиль
    @POST("cars")
    suspend fun createCar(
        @Body car: CarDto
    ): Response<List<CarDto>>

    // Service Requests - получаем заявки для автомобиля
    @GET("service_requests")
    suspend fun getRequestsByCar(
        @Query("car_id") carId: String
    ): Response<List<ServiceRequestDto>>

    // Service Requests - создаем заявку
    @POST("service_requests")
    suspend fun createServiceRequest(
        @Body request: ServiceRequestDto
    ): Response<List<ServiceRequestDto>>

    // Service Requests - обновляем статус заявки (отмена)
    @PATCH("service_requests")
    suspend fun updateRequestStatus(
        @Query("id") id: String,
        @Body update: Map<String, String>
    ): Response<List<ServiceRequestDto>>
}