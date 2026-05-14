package com.example.data.remote

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(json.asConverterFactory(
                "application/json; charset=utf-8".toMediaType()))
            .build()

    val apiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)
}