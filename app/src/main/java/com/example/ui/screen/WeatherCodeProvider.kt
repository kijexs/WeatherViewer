package com.example.ui.screen

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

fun Int.toWeatherDescription(): String = when (this) {
    0 -> "ясно"
    in 1..3 -> "переменная облачность"
    in 45..48 -> "туман"
    in 51..55 -> "моросящий дождь"
    in 61..65 -> "дождь"
    in 71..75 -> "снегопад"
    in 80..82 -> "ливень"
    95 -> "гроза"
    else -> "неизвестно"
}

//@Composable
//fun WeatherIcon(code: Int) {
//    val icon = when (code) {
//            ...
//    }
//    Icon(icon, contentDescription = null)
//}