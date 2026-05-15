package com.example.weatherviewer.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.weatherviewer.R

fun Int.toWeatherDescription(): String = when (this) {
    0 -> "ясно"
    in 1..2 -> "переменная облачность"
    3 -> "облачно"
    in 45..48 -> "туман"
    in 51..57 -> "моросящий дождь"
    61 -> "слабый дождь"
    63, 65 -> "дождь"
    66, 67 -> "снег с дождем"
    71, 73, 77 -> "снег"
    in 80..82 -> "ливень"
    75, 85, 86 -> "снегопад"
    95 -> "гроза"
    96, 99 -> "гроза с градом"
    else -> "неизвестно"
}

@Composable
fun WeatherIcon(code: Int, size: Dp = 24.dp) {
    val imageId = when (code) {
        0 -> R.drawable.ic_sun
        in 1..2 -> R.drawable.ic_cloudy_sunny
        3 ->  R.drawable.ic_cloudy
        in 45..48 ->  R.drawable.ic_fog
        in 51..57 ->  R.drawable.ic_rainy_light
        61 ->  R.drawable.ic_rain
        in  80..82, 63, 65 ->  R.drawable.ic_heavy_rain
        66, 67 -> R.drawable.ic_frezzy_rainy
        71, 73, 77 ->  R.drawable.ic_snowy_light
        75, 85, 86 ->  R.drawable.ic_snowy
        in 95..99 ->  R.drawable.ic_storm
        else -> R.drawable.ic_question_mark
    }
    Box(
        modifier = Modifier
            .size(size + 20.dp)
            .background(
                color = Color.Black.copy(alpha = 0.15f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Погода",
            modifier = Modifier
                .size(48.dp)
        )
    }
}

@Composable
fun weatherCardColor(code: Int): Color {
    val isDark = isSystemInDarkTheme()
    return when (code){
        0 -> if (isDark) Color(0xFF3E2723) else Color(0xFFFFF9C4)
        in 1..3 -> if (isDark) Color(0xFF37474F) else Color(0xFFE0E0E0)
        in 51..55, in 61..65 -> if (isDark) Color(0xFF1A237E) else Color(0xFFBBDEFB)
        in 71..77 -> if (isDark) Color(0xFF263238) else Color(0xFFE3F2FD)
        in 80..99 -> if (isDark) Color(0xFF311B92) else Color(0xFFBDBDBD)
        else -> if (isDark) Color.DarkGray else Color.White
    }
}