package com.example.weatherviewer.data.local

import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.DailyForecast
import com.example.weatherviewer.data.model.HourlyForecast
import com.example.weatherviewer.data.remote.Current
import com.example.weatherviewer.data.remote.Daily
import com.example.weatherviewer.data.remote.Hourly

fun DailyEntity.toDailyForecast(): DailyForecast {
    return DailyForecast(
        date = this.date,
        minTemperature = this.minTemperature,
        maxTemperature = this.maxTemperature,
        weatherCode = this.weatherCode
    )
}

fun HourlyEntity.toHourlyForecast(): HourlyForecast {
    return HourlyForecast(
        time = this.time,
        temperature = this.temperature,
        weatherCode = this.weatherCode
    )
}

fun CurrentWeatherEntity.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        temperature = this.temperature,
        weatherCode = this.weatherCode,
        windSpeed = this.windSpeed
    )
}

fun Current.toCurrentWeatherEntity(): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        id = 1,
        temperature = this.temperature,
        weatherCode = this.weather_code,
        windSpeed = this.windspeed_10m
    )
}

fun Daily.toDailyEntities(): List<DailyEntity> {
    return time.indices.map { i ->
        DailyEntity(
            date = time[i],
            maxTemperature = temperature_2m_max[i],
            minTemperature = temperature_2m_min[i],
            weatherCode = weather_code[i]
        )
    }
}

fun Hourly.toHourlyEntities(): List<HourlyEntity> {
    return time.indices.map { i ->
        HourlyEntity(
            time = time[i],
            temperature = temperature[i],
            weatherCode = weather_code[i]
        )
    }
}
