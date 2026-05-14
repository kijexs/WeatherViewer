package com.example.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SettingsSystemDaydream
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Int.toWeatherDescription(): String = when (this) {
    0 -> "ясно"
    in 1..3 -> "переменная облачность"
    in 45..48 -> "туман"
    in 51..57 -> "моросящий дождь"
    in 61..67 -> "дождь"
    in 71..77 -> "снегопад"
    in 80..82 -> "ливень"
    in 85..86 -> "снежный ливень"
    95 -> "гроза"
    96, 99 -> "гроза с градом"
    else -> "неизвестно"
}

@Composable
fun WeatherIcon(code: Int, size: Dp = 24.dp) {
    val icon = when (code) {
        0 -> Icons.Filled.WbSunny
        in 1..3 -> Icons.Filled.Cloud
        in 45..48 -> Icons.Filled.SettingsSystemDaydream
        in 51..57, in 61..67 -> Icons.Filled.Umbrella
        in 71..77 -> Icons.Filled.AcUnit
        in 80..99 -> Icons.Filled.Thunderstorm
        else -> Icons.Filled.QuestionMark
    }
    Icon(
        imageVector = icon,
        contentDescription = "Погода"
    )
}

@Composable
fun weatherCardColor(code: Int): Color = when (code) {
    0 -> Color(0xFFFFF9C4)
    in 1..3 -> Color(0xFFE0E0E0)
    in 51..55, in 61..65 -> Color(0xFFBBDEFB)
    in 71..77 -> Color(0xFFE3F2FD)
    in 80..99 -> Color(0xFFBDBDBD)
    else -> Color.White
}