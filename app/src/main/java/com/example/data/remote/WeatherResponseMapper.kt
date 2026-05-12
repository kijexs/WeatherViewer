package com.example.data.remote

import com.example.data.model.WeatherForecast

fun WeatherResponse.toWeatherForecast(): List<WeatherForecast> {
    val result: MutableList<WeatherForecast> = mutableListOf()
    val daily = this.daily
    for (i in daily.time.indices) {
        val weatherForecast = WeatherForecast(daily.time[i],daily.temperature_2m_min[i],daily.temperature_2m_max[i],daily.weather_code[i])
        result.run { add(weatherForecast) }
    }
    return result
}