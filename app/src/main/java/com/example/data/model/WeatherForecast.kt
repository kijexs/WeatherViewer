package com.example.data.model

data class CurrentWeather(
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double
)

data class HourlyForecast(
    val time: String,
    val temperature: Double,
    val weatherCode: Int
)

data class DailyForecast(
    val date: String,
    val minTemperature: Double,
    val maxTemperature: Double,
    val weatherCode: Int
)
data class WeatherForecast (
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>
)