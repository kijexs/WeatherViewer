package com.example.weatherviewer.data.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherMappersTest {

    private lateinit var forecast: WeatherForecast

    @Before
    fun setUp() {
        forecast = WeatherForecast(
            current = CurrentWeather(
                temperature = 22.0,
                weatherCode = 0,
                windSpeed = 5.0
            ),
            hourly = listOf(
                HourlyForecast("2026-05-15T14:00", 10.0, 0),
                HourlyForecast("2026-05-15T15:00", 11.0, 1),
                HourlyForecast("2026-05-15T16:00", 12.0, 3),
                HourlyForecast("2026-05-15T17:00", 13.0, 0),
                HourlyForecast("2026-05-15T18:00", 14.0, 1),
                HourlyForecast("2026-05-15T19:00", 15.0, 3)
            ),
            daily = listOf(
                DailyForecast("2026-05-15", 15.0, 19.0, 0),
                DailyForecast("2026-05-16", 13.0, 17.0, 3)
            )
        )
    }

    @Test
    fun `hoursForDay should return all hours for given date`() {
        val hourlyForecastList = forecast.hoursForDay("2026-05-15")

        assertEquals(6, hourlyForecastList.size)
    }

    @Test
    fun `hoursForDay should return empty list when no hours match date`() {
        val hourlyForecastList = forecast.hoursForDay("2026-05-16")

        assertEquals(0, hourlyForecastList.size)
    }

    @Test
    fun `everyThird should keep every third element from list`() {
        val hourlyForecastList = forecast.hourly.everyThird()

        assertEquals(2, hourlyForecastList.size)
        assertEquals("2026-05-15T14:00", hourlyForecastList[0].time)
        assertEquals("2026-05-15T17:00", hourlyForecastList[1].time)
    }

    @Test
    fun `everyThird should return single element when list has 3 items`() {
        val hourlyForecastList = forecast.hourly.subList(0,3).everyThird()

        assertEquals(1 ,hourlyForecastList.size)
        assertEquals("2026-05-15T14:00", hourlyForecastList[0].time)
    }

    @Test
    fun `everyThird should return empty list for empty input`() {
        assertEquals(0, emptyList<HourlyForecast>().everyThird().size)
    }

    @Test
    fun `everyThird should return first element for list with 2 items`() {
        val hourlyForecastList = forecast.hourly.subList(3,4).everyThird()

        assertEquals(1 ,hourlyForecastList.size)
        assertEquals("2026-05-15T17:00", hourlyForecastList[0].time)
    }

    @Test
    fun `toDayDisplay should format date as 'day month'`() {
        val day = forecast.daily[0].date.toDayDisplay()

        assertEquals("15 мая",day)
    }

    @Test
    fun `toDayWithWeekday should format date as 'day month weekday'`() {
        val day = forecast.daily[0].date.toDayWithWeekday()

        assertEquals("пятница, 15 мая",day)
    }

    @Test
    fun `toWeatherDescription should return 'ясно' for code 0`() {
        assertEquals("ясно",0.toWeatherDescription())
    }

    @Test
    fun `toWeatherDescription should return 'дождь' for rain codes`() {
        assertEquals("дождь",63.toWeatherDescription())
    }

    @Test
    fun `toWeatherDescription should return 'неизвестно' for unknown code`() {
        assertEquals("неизвестно",999.toWeatherDescription())
    }

    @Test
    fun `toHourDisplay should extract time from datetime string`() {
        assertEquals("14:00", "2026-05-15T14:00".toHourDisplay())
    }

    @Test
    fun `toHourDisplay should handle midnight`() {
        assertEquals("00:00", "2026-05-15T00:00".toHourDisplay())
    }
}
