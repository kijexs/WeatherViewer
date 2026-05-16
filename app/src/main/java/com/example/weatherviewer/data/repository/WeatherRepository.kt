package com.example.weatherviewer.data.repository

import com.example.weatherviewer.data.local.WeatherDao
import com.example.weatherviewer.data.local.toCurrentWeather
import com.example.weatherviewer.data.local.toCurrentWeatherEntity
import com.example.weatherviewer.data.local.toDailyEntities
import com.example.weatherviewer.data.local.toDailyForecast
import com.example.weatherviewer.data.local.toHourlyEntities
import com.example.weatherviewer.data.local.toHourlyForecast
import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.WeatherForecast
import com.example.weatherviewer.data.remote.WeatherApiService
import com.example.weatherviewer.data.remote.takeNext5Hours
import com.example.weatherviewer.data.remote.toWeatherForecast
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class WeatherRepository @Inject constructor (
    private val apiService: WeatherApiService,
    private val weatherDao: WeatherDao
){
     fun getWeatherForecast(latitude: Double, longitude: Double ): Flow<WeatherForecast> = flow {
         try {
             val weatherResponse = apiService.getWeatherForecast(latitude, longitude)
             val currentWeatherEntity = weatherResponse.current.toCurrentWeatherEntity()
             val dailyEntity = weatherResponse.daily.toDailyEntities()
             val hourlyEntity = weatherResponse.hourly.toHourlyEntities()
             weatherDao.deleteAllDaily()
             weatherDao.deleteAllHourly()
             weatherDao.insertCurrentWeather(currentWeatherEntity)
             weatherDao.insertDaily(dailyEntity)
             weatherDao.insertHourly(hourlyEntity)

             val forecastList = weatherResponse.toWeatherForecast()
             emit(forecastList)
         } catch (e: Exception) {
             val currentEntity = weatherDao.getCurrentWeather()
             val dailyEntities = weatherDao.getDailyForecast()
             val hourlyEntities = weatherDao.getHourlyForecast()

             if (currentEntity != null) {
                 val hourly = hourlyEntities.map { it.toHourlyForecast() }
                 val daily = dailyEntities.map { it.toDailyForecast() }
                 val currentHour = hourly.firstOrNull()
                 val current: CurrentWeather
                 if (currentHour != null) {
                     current = CurrentWeather(
                         temperature = currentHour.temperature,
                         weatherCode = currentHour.weatherCode,
                         windSpeed = null
                     )
                 }
                 else current = currentEntity.toCurrentWeather()
                 val forecast = WeatherForecast(
                     current = current,
                     daily = daily,
                     hourly = hourly
                 )
                 emit(forecast)
             } else {
                 throw Exception("нет кэша")
             }
         }
    }
}
