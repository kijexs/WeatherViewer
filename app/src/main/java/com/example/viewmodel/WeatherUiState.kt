package com.example.viewmodel

import com.example.data.model.WeatherForecast

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val forecast: WeatherForecast) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}