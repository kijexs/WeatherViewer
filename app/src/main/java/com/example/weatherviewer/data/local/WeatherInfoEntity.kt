package com.example.weatherviewer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val downloadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_forecast")
data class DailyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val forecastId: Int = 1,
    val date: String,
    val maxTemperature: Double,
    val minTemperature: Double,
    val weatherCode: Int
)


@Entity(tableName = "hourly_forecast")
data class HourlyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val forecastId: Int = 1,
    val time: String,
    val temperature: Double,
    val weatherCode: Int
)