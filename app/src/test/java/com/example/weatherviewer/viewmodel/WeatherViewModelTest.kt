package com.example.weatherviewer.viewmodel

import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.DailyForecast
import com.example.weatherviewer.data.model.HourlyForecast
import com.example.weatherviewer.data.model.WeatherForecast
import com.example.weatherviewer.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.RuntimeException
import app.cash.turbine.test
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var repository: WeatherRepository
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    companion object {
        val fakeForecast = WeatherForecast(
            current = CurrentWeather(22.0, 0, 5.0),
            daily = listOf(DailyForecast("2026-05-15", 25.0, 15.0, 0)),
            hourly = listOf(HourlyForecast("2026-05-15T14:00", 10.0, 0))
        )

        val fakeForecastAnother = WeatherForecast(
            current = CurrentWeather(23.0, 0, 6.0),
            daily = listOf(DailyForecast("2026-05-16", 26.0, 22.0, 0)),
            hourly = listOf(HourlyForecast("2026-05-16T14:00", 15.0, 3))
        )
    }

    @Test
    fun `initial state should have loading state`() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)

        val viewModel = WeatherViewModel(repository)

        viewModel.uiState.test {
            assertEquals(WeatherUiState.Loading, awaitItem())

            val success = awaitItem()

            assertTrue(success is WeatherUiState.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh should restart loading`() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)

        val viewModel = WeatherViewModel(repository)

       viewModel.uiState.test {
           assertEquals(WeatherUiState.Loading, awaitItem())
           assertTrue(awaitItem() is WeatherUiState.Success)

           coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecastAnother)

           viewModel.refresh()
           advanceUntilIdle()

           assertEquals(WeatherUiState.Loading, awaitItem())
           assertTrue(awaitItem() is WeatherUiState.Success)

           cancelAndIgnoreRemainingEvents()
       }
    }

    @Test
    fun `error should set Error state`() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flow {
            throw RuntimeException("нет сети")
        }

        val viewModel = WeatherViewModel(repository)

        viewModel.uiState.test {
            assertEquals(WeatherUiState.Loading, awaitItem())

            val error = awaitItem()
            assertTrue(error is WeatherUiState.Error)
            assertEquals("нет сети", (error as WeatherUiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }
}