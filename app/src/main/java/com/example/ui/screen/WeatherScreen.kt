package com.example.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.WeatherForecast
import com.example.viewmodel.WeatherUiState
import com.example.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Погода") }) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val currentState = state) {
                is WeatherUiState.Loading -> LoadingContent()
                is WeatherUiState.Error -> ErrorContent(
                    message = currentState.message,
                    onRetry = { viewModel.refresh() }
                )
                is WeatherUiState.Success -> ForecastList(currentState.forecast)
            }
        }
    }
}

@Composable
fun ForecastList(forecast: List<WeatherForecast>) {
    LazyColumn {
        items(forecast) { day ->
            DayCard(day)
        }
    }
}

@Composable
fun DayCard(day: WeatherForecast) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = day.date)
            Text(text = "Мин: ${day.minTemperature}°C")
            Text(text = "Макс: ${day.maxTemperature}°C")
            Text(text = day.weatherCode.toWeatherDescription())
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Произошла ошибка",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = message,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onRetry) {
                Text("Повторить")
            }
        }

    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text(
                text = "Loading...",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
