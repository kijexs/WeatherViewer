package com.example.weatherviewer.data.remote

import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.DailyForecast
import com.example.weatherviewer.data.model.HourlyForecast
import com.example.weatherviewer.data.model.WeatherForecast
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherResponseMapperTest {

    private lateinit var weatherResponse: WeatherResponse

    @Before
    fun setUp() {
        weatherResponse = WeatherResponse(
            current = Current(
                temperature = 22.0,
                weather_code = 0,
                windspeed_10m = 5.0
            ),
            hourly = Hourly(
                time = listOf("2026-05-15T14:00", "2026-05-15T15:00"),
                temperature = listOf(10.0, 11.0),
                weather_code = listOf(0, 0)
            ),
            daily = Daily(
                time = listOf("2026-05-15", "2026-05-16"),
                temperature_2m_max = listOf(25.0, 23.0),
                temperature_2m_min = listOf(15.0, 12.0),
                weather_code = listOf(0, 3)
            )
        )
    }

    private val current = CurrentWeather(22.0, 0, 5.0)
    private val dailyForecastFirst = DailyForecast("2026-05-15", 15.0, 25.0, 0)
    private val dailyForecastSecond = DailyForecast("2026-05-16", 12.0, 23.0, 3)
    private val hourlyForecastFirst = HourlyForecast("2026-05-15T14:00", 10.0, 0)
    private val hourlyForecastSecond = HourlyForecast("2026-05-15T15:00", 11.0, 0)

    @Test
    fun `toWeatherForecast should transform dto to WeatherForecast data class`() {
        val weatherForecast = weatherResponse.toWeatherForecast()

        val assertWeatherForecast = WeatherForecast(
            current,
            listOf(hourlyForecastFirst, hourlyForecastSecond),
            listOf(dailyForecastFirst, dailyForecastSecond)
        )

        assertEquals(assertWeatherForecast,weatherForecast)
    }

    @Test
    fun `toDailyForecast should transform daily dto to DailyForecast data class`() {
        val dailyForecast = weatherResponse.daily.toDailyForecast()

        assertEquals(listOf(dailyForecastFirst, dailyForecastSecond),dailyForecast)
    }

    @Test
    fun `toHourlyForecast should transform hourly dto to HourlyForecast data class`() {
        val hourlyForecast = weatherResponse.hourly.toHourlyForecast()

        assertEquals(listOf(hourlyForecastFirst, hourlyForecastSecond),hourlyForecast)
    }

    @Test
    fun `toCurrentForecast should transform current dto to CurrentForecast data class`() {
        val currentWeather = weatherResponse.current.toCurrentForecast()

        assertEquals(current, currentWeather)
    }
}