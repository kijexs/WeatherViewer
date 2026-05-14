package com.example.weatherviewer.data.remote

import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.DailyForecast
import com.example.weatherviewer.data.model.HourlyForecast
import com.example.weatherviewer.data.model.WeatherForecast
import java.time.LocalDateTime

fun WeatherResponse.toWeatherForecast():WeatherForecast {
    val dailyForecastList: MutableList<DailyForecast> = mutableListOf()
    val daily = this.daily
    for (i in daily.time.indices) {
        val dailyForecast = DailyForecast(
            daily.time[i],
            daily.temperature_2m_min[i],
            daily.temperature_2m_max[i],
            daily.weather_code[i]
        )
        dailyForecastList.add(dailyForecast)
    }
    val now = LocalDateTime.now()
    val hourlyForecastList: MutableList<HourlyForecast> = mutableListOf()
    val hourly = this.hourly
    var count = 0
    for (i in hourly.time.indices) {
        val hourTime = LocalDateTime.parse(hourly.time[i])
        if (hourTime >= now) {
            if (count < 5) {
                val hourlyForecast = HourlyForecast(
                    hourly.time[i],
                    hourly.temperature[i],
                    hourly.weather_code[i]
                )
                hourlyForecastList.add(hourlyForecast)
                count++
            }
            else break
        }
    }
    val current = this.current
    val currentForecast = CurrentWeather(
        current.temperature,
        current.weather_code,
        current.windspeed_10m
        )
    return WeatherForecast(currentForecast, hourlyForecastList, dailyForecastList)
}