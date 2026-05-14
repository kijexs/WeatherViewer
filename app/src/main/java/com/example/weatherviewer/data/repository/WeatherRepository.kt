package com.example.weatherviewer.data.repository

import com.example.weatherviewer.data.model.WeatherForecast
import com.example.weatherviewer.data.remote.WeatherApiService
import com.example.weatherviewer.data.remote.toWeatherForecast
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class WeatherRepository @Inject constructor (
    private val apiService: WeatherApiService
){
     fun getWeatherForecast(latitude: Double, longitude: Double ): Flow<WeatherForecast> = flow {
         val weatherResponse = apiService.getWeatherForecast(latitude, longitude)
         val forecastList = weatherResponse.toWeatherForecast()
         emit(forecastList)
    }
}