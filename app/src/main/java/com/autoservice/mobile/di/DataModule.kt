package com.autoservice.mobile.di

import android.content.Context
import com.autoservice.mobile.data.remote.api.SupabaseApi
import com.autoservice.mobile.data.repository.AuthRepositoryImpl
import com.autoservice.mobile.data.repository.CarRepositoryImpl
import com.autoservice.mobile.data.repository.ServiceRequestRepositoryImpl
import com.autoservice.mobile.domain.repository.AuthRepository
import com.autoservice.mobile.domain.repository.CarRepository
import com.autoservice.mobile.domain.repository.ServiceRequestRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {

    // Network
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder  = original.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer $SUPABASE_KEY")
                    .addHeader("Prefer", "return=representation")
                    .build()

                val request = requestBuilder.newBuilder().build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("$SUPABASE_URL/rest/v1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<SupabaseApi> {
        get<Retrofit>().create(SupabaseApi::class.java)
    }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), androidContext()) }
    single<CarRepository> { CarRepositoryImpl(get()) }
    single<ServiceRequestRepository> { ServiceRequestRepositoryImpl(get()) }
}

private const val SUPABASE_URL = "https://xogbpvxwffyyrnzygrob.supabase.co"
private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhvZ2Jwdnh3ZmZ5eXJuenlncm9iIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQ0Mzk0MTgsImV4cCI6MjA4MDAxNTQxOH0.7WwjIbUuoFVHqV1hs7n5kwqSM6WQnwa7OdZE9M7Ay2E"
