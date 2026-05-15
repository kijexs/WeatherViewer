package com.example.weatherviewer.data.remote

import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.DailyForecast
import com.example.weatherviewer.data.model.HourlyForecast
import com.example.weatherviewer.data.model.WeatherForecast
import java.time.LocalDateTime

fun WeatherResponse.toWeatherForecast():WeatherForecast {
    val daily = this.daily.toDailyForecast()

    val hourly = this.hourly.toHourlyForecast()

    val current = this.current.toCurrentForecast()

    return WeatherForecast(current, hourly, daily)
}

fun Hourly.toHourlyForecast(): List<HourlyForecast> {
    return time.indices.map { i ->
        HourlyForecast(
            time = time[i],
            temperature = temperature[i],
            weatherCode = weather_code[i]
        )
    }
}

fun Daily.toDailyForecast(): List<DailyForecast> {
    return time.indices.map { i->
        DailyForecast(
            date = time[i],
            minTemperature = temperature_2m_min[i],
            maxTemperature = temperature_2m_max[i],
            weatherCode = weather_code[i]
        )
    }
}

fun Current.toCurrentForecast(): CurrentWeather {
    return CurrentWeather(
        temperature = temperature,
        weatherCode = weather_code,
        windSpeed = windspeed_10m
    )
}

fun List<HourlyForecast>.takeNext5Hours(): List<HourlyForecast> {
    val now = LocalDateTime.now()
    var count = 0
    return this.filter { hour ->
        val hourTime = LocalDateTime.parse(hour.time)
        if (hourTime >= now && count < 5) {
            count++
            true
        } else false
    }
}