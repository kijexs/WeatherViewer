package com.example.weatherviewer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(current: CurrentWeatherEntity)

    @Query("SELECT * FROM current_weather WHERE id = 1")
    suspend fun getCurrentWeather(): CurrentWeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaily(daily: List<DailyEntity>)

    @Query("SELECT * FROM daily_forecast ORDER BY date")
    suspend fun getDailyForecast(): List<DailyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourly(hourly: List<HourlyEntity>)

    @Query("SELECT * FROM hourly_forecast ORDER BY time")
    suspend fun getHourlyForecast(): List<HourlyEntity>

    @Query("DELETE FROM daily_forecast")
    suspend fun deleteAllDaily()

    @Query("DELETE FROM hourly_forecast")
    suspend fun deleteAllHourly()
}
