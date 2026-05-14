package com.example.data.remote

import kotlinx.serialization.Serializable
@Serializable
data class WeatherResponse(
    val daily: Daily,
    val hourly: Hourly,
    val current: Current
)

@Serializable
data class Daily(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val weather_code: List<Int>
)
@Serializable
data class Hourly(
    val time: List<String>,
    val temperature: List<Double>,
    val weather_code: List<Int>
)
@Serializable
data class Current(
    val temperature: Double,
    val weather_code: Int,
    val windspeed_10m: Double
)