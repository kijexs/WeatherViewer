package com.example.weatherviewer.data.model

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

fun String.toDayDisplay(): String {
    val date: List<String> = this.split("-")
    val result: String = when (date[1].toInt())
    {
        1 -> "${date[2]} января"
        2 -> "${date[2]} февраля"
        3 -> "${date[2]} марта"
        4 -> "${date[2]} апреля"
        5 -> "${date[2]} мая"
        6 -> "${date[2]} июня"
        7 -> "${date[2]} июля"
        8 -> "${date[2]} августа"
        9 -> "${date[2]} сентября"
        10 -> "${date[2]} октября"
        11 -> "${date[2]} ноября"
        12 -> "${date[2]} декабря"
        else -> "неизвестно"
    }
    return result
}

fun String.toDayWithWeekday(): String {
    val date = LocalDate.parse(this)
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))
    val dayAndMonth = this.toDayDisplay()
    return "$dayOfWeek, $dayAndMonth"
}

fun String.toHourDisplay(): String = this.substringAfter("T")
