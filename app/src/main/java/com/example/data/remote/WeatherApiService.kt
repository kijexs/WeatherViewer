package com.example.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/forecast")
    fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String = "Europe/Moscow",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code"
    ): WeatherResponse

}