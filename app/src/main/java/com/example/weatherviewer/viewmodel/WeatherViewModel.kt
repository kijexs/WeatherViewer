package com.example.weatherviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherviewer.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    companion object {
        const val DEFAULT_LAT = 59.8833
        const val DEFAULT_LON = 29.9
    }

    private val _coordinates = MutableStateFlow(DEFAULT_LAT to DEFAULT_LON)
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<WeatherUiState> = _coordinates
        .flatMapLatest { (lat: Double, lon: Double) ->
            repository.getWeatherForecast(lat, lon)
                .map { forecast -> WeatherUiState.Success(forecast) as WeatherUiState }
                .onStart { emit(WeatherUiState.Loading) }
                .catch { e -> emit(WeatherUiState.Error(e.message ?: "Неизвестная ошибка")) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), WeatherUiState.Loading)

    fun refresh() {
        _coordinates.value = 0.0 to 0.0  // триггерим flatMapLatest
        _coordinates.value = DEFAULT_LAT to DEFAULT_LON
    }
}