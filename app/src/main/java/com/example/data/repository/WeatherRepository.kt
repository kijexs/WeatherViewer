package com.example.data.repository

import com.example.data.model.WeatherForecast
import com.example.data.remote.WeatherApiService
import com.example.data.remote.toWeatherForecast
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class WeatherRepository @Inject constructor (
    private val apiService: WeatherApiService
){
     fun getWeatherForecast(latitude: Double, longitude: Double ): Flow<List<WeatherForecast>> = flow {
         val weatherResponse = apiService.getWeatherForecast(latitude, longitude)
         val forecastList = weatherResponse.toWeatherForecast()
         emit(forecastList)
    }

}