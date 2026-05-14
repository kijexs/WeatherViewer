package com.example.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CurrentWeather
import com.example.data.model.DailyForecast
import com.example.data.model.HourlyForecast
import com.example.data.model.WeatherForecast
import com.example.data.model.toDayDisplay
import com.example.data.model.toHourDisplay
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
fun ForecastList(forecast: WeatherForecast) {
    LazyColumn {
        item { CurrentWeatherSection(forecast.current) }

        item {
            Text(
                text = "Почасовой прогноз",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            LazyRow {
                items (forecast.hourly){ hour ->
                    HourlyCard(hour)
                }
            }
        }

        item {
            Text(
                text = "Прогноз на 7 дней",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(forecast.daily) { day ->
            DayCard(day)
        }
    }
}

@Composable
fun CurrentWeatherSection(currentWeather: CurrentWeather) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Петергоф",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${currentWeather.temperature}°C",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = currentWeather.weatherCode.toWeatherDescription(),
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Ветер: ${currentWeather.windSpeed} м/с",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun HourlyCard(hour: HourlyForecast) {
    Card(modifier = Modifier
        .padding(4.dp)
        .width(70.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = hour.time.toHourDisplay(),
                style = MaterialTheme.typography.bodySmall
            )
            WeatherIcon(code = hour.weatherCode)
            Text(
                text = "${hour.temperature.toInt()}°C",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun DayCard(day: DailyForecast) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = weatherCardColor(day.weatherCode)
        )
    ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp),
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically
       ) {
           Column(modifier = Modifier.weight(1f)) {
               Text(text = day.date.toDayDisplay())
               Text(text = "${day.maxTemperature}° / ${day.minTemperature}°")
               Text(text = day.weatherCode.toWeatherDescription())
           }
           WeatherIcon(code = day.weatherCode, size = 40.dp)
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
