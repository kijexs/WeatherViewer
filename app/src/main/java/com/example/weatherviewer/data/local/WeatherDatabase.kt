package com.example.weatherviewer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CurrentWeatherEntity::class, DailyEntity::class, HourlyEntity:: class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}