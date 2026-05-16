package com.example.weatherviewer.data.repository

import com.example.weatherviewer.data.local.WeatherDao
import com.example.weatherviewer.data.local.toCurrentWeatherEntity
import com.example.weatherviewer.data.local.toDailyEntities
import com.example.weatherviewer.data.local.toHourlyEntities
import com.example.weatherviewer.data.remote.Current
import com.example.weatherviewer.data.remote.Daily
import com.example.weatherviewer.data.remote.Hourly
import com.example.weatherviewer.data.remote.WeatherApiService
import com.example.weatherviewer.data.remote.WeatherResponse
import com.example.weatherviewer.data.remote.toWeatherForecast
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private lateinit var apiService: WeatherApiService
    private lateinit var weatherDao: WeatherDao
    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        apiService = mockk(relaxed = true)
        weatherDao = mockk(relaxed = true)
        repository = WeatherRepository(apiService, weatherDao)
    }

    companion object {
        val fakeResponse = WeatherResponse(
            current = Current(22.0, 0, 5.0),
            daily = Daily(listOf("2026-05-15"), listOf(25.0), listOf(15.0), listOf(0)),
            hourly = Hourly(listOf("2026-05-15T14:00"), listOf(10.0), listOf(0))
        )
    }

    @Test
    fun `getWeatherForecast should return forecast from API and save to cache`() = runBlocking {
        coEvery { apiService.getWeatherForecast(any(), any()) } returns fakeResponse
        coEvery { weatherDao.insertCurrentWeather(any()) } returns Unit
        coEvery { weatherDao.insertHourly(any()) } returns Unit
        coEvery { weatherDao.insertDaily(any()) } returns Unit

        val result = repository.getWeatherForecast(59.8833, 29.9).collect { forecast ->
            assertEquals(fakeResponse.toWeatherForecast(), forecast)
            return@collect
        }

        coVerify { weatherDao.insertCurrentWeather(any()) }
        coVerify { weatherDao.insertHourly(any()) }
        coVerify { weatherDao.insertDaily(any()) }
    }

    @Test
    fun `getWeatherForecast should return cached data when API fails`() = runBlocking {
        coEvery { apiService.getWeatherForecast(any(), any()) } throws RuntimeException("нет сети")
        coEvery { weatherDao.getCurrentWeather() } returns fakeResponse.current.toCurrentWeatherEntity()
        coEvery { weatherDao.getDailyForecast() } returns fakeResponse.daily.toDailyEntities()
        coEvery { weatherDao.getHourlyForecast() } returns fakeResponse.hourly.toHourlyEntities()

        val result = repository.getWeatherForecast(59.8833, 29.9).first()

        coVerify { weatherDao.getCurrentWeather() }
        coVerify { weatherDao.getDailyForecast() }
        coVerify { weatherDao.getHourlyForecast() }
    }

    @Test
    fun `getWeatherForecast should throw exception when API fails and no cache`() = runBlocking {
        coEvery { apiService.getWeatherForecast(any(), any()) } throws RuntimeException("нет сети")
        coEvery { weatherDao.getCurrentWeather() } returns null

        try {
            repository.getWeatherForecast(59.8833, 29.9).first()
            fail("Expected exception")
        }
        catch (e: Exception) {
            assertEquals("нет кэша", e.message)
        }
    }
}