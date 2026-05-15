package com.example.weatherviewer.data.model

import com.example.weatherviewer.data.remote.takeNext5Hours

data class CurrentWeather(
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double?
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

fun WeatherForecast.hoursForDay(date: String): List<HourlyForecast> {
    return hourly.filter { it.time.startsWith(date) }
}

fun List<HourlyForecast>.everyThird(): List<HourlyForecast> {
    return this.filterIndexed { index, _ ->  index % 3 == 0}
}

fun WeatherForecast.nextHours(): List<HourlyForecast> {
    return hourly.takeNext5Hours()
}