package com.example.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val daily: Daily,
)

@Serializable
data class Daily(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val weather_code: List<Int>
)