package com.autoservice.mobile.di

import com.autoservice.mobile.ui.viewmodel.AuthViewModel
import com.autoservice.mobile.ui.viewmodel.CarsViewModel
import com.autoservice.mobile.ui.viewmodel.NewRequestsViewModel
import com.autoservice.mobile.ui.viewmodel.RequestsViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<AuthViewModel> { AuthViewModel(get(), get(), get(), get()) }
    single<CarsViewModel> { CarsViewModel(get(), get(), get(), get()) }
    single<RequestsViewModel> { RequestsViewModel(get(), get()) }
    single<NewRequestsViewModel> { NewRequestsViewModel(get(), get(), get(), get()) }
}
