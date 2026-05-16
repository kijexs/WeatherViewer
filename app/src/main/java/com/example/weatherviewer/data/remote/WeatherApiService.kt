package com.example.weatherviewer.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature,weather_code,windspeed_10m",
        @Query("timezone") timezone: String = "Europe/Moscow",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("hourly") hourly: String = "temperature,weather_code"
    ): WeatherResponse

}
