package com.autoservice.mobile.di


import com.autoservice.mobile.domain.usecase.auth.*
import com.autoservice.mobile.domain.usecase.cars.*
import com.autoservice.mobile.domain.usecase.requests.*
import org.koin.dsl.module

val domainModule = module {

    // Auth Use Cases
    factory { LoginUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { IsUserLoggedInUseCase(get()) }
    factory { LogoutUseCase(get()) }

    // Cars Use Cases
    factory { GetUserCarsUseCase(get()) }
    factory { LoadUserCarsUseCase(get()) }
    factory { AddCarUseCase(get()) }
    factory { RefreshUserCarsUseCase(get()) }

    // Service Requests Use Cases
    factory { GetUserRequestsUseCase(get()) }
    factory { LoadUserRequestsUseCase(get()) }
    factory { CreateServiceRequestUseCase(get()) }
    factory { CancelRequestUseCase(get()) }
    factory { RefreshUserRequestsUseCase(get()) }
}
