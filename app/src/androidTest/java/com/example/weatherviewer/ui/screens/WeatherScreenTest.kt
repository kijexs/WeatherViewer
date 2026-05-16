package com.example.weatherviewer.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.DailyForecast
import com.example.weatherviewer.data.model.HourlyForecast
import com.example.weatherviewer.data.model.WeatherForecast
import com.example.weatherviewer.data.repository.WeatherRepository
import com.example.weatherviewer.ui.screen.WeatherScreen
import com.example.weatherviewer.viewmodel.WeatherUiState
import com.example.weatherviewer.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
@LargeTest
class WeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
    }

    companion object {
        val fakeForecast = WeatherForecast(
            current = CurrentWeather(22.0, 0, 5.0),
            daily = listOf(DailyForecast("2026-05-15", 25.0, 15.0, 0)),
            hourly = listOf(HourlyForecast("2026-05-15T14:00", 10.0, 0))
        )
    }

    @Test
    fun displaysCurrentWeatherOnSuccess() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("22.0°C").assertIsDisplayed()
        composeTestRule.onNodeWithText("ясно").assertIsDisplayed()
    }

    @Test
    fun displaysLoadingState() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf()

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()

    }

    @Test
    fun displaysErrorState() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flow {
            throw RuntimeException("нет сети")
        }

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Произошла ошибка").assertIsDisplayed()
        composeTestRule.onNodeWithText("нет сети").assertIsDisplayed()
        composeTestRule.onNodeWithText("Повторить").assertIsDisplayed()
    }

    @Test
    fun clickingDayCardShowsHourlyDetails() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("пятница, 15 мая").performClick()
        composeTestRule.onNodeWithText("14:00").assertIsDisplayed()
    }

    @Test
    fun retryButtonCallsRefresh() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flow {
            throw RuntimeException("нет сети")
        }

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Повторить").performClick()
        coVerify { repository.getWeatherForecast(any(), any()) }
    }

    @Test
    fun displaysNoInternetWhenWindSpeedIsNull() = runTest {
        val forecastWithoutWind = WeatherForecast(
            current = CurrentWeather(22.0, 0, windSpeed = null),
            daily = listOf(DailyForecast("2026-05-15", 15.0, 25.0, 0)),
            hourly = listOf(HourlyForecast("2026-05-15T14:00", 10.0, 0))
        )

        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(forecastWithoutWind)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("отсутствует подключение к интернету").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ветер:").assertDoesNotExist()
    }

    @Test
    fun displaysHourlyForecast() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Почасовой прогноз").assertIsDisplayed()
        composeTestRule.onNodeWithText("14:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("10°C").assertIsDisplayed()
    }

    @Test
    fun displaysDailyForecast() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Прогноз на 7 дней").assertIsDisplayed()
        composeTestRule.onNodeWithText("пятница, 15 мая").assertIsDisplayed()
    }

    @Test
    fun displaysCityName() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Петергоф").assertIsDisplayed()
    }

    @Test
    fun retryButtonNotShownOnSuccess() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(fakeForecast)
        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent { WeatherScreen(viewModel = viewModel) }

        composeTestRule.onNodeWithText("Повторить").assertDoesNotExist()
    }
}
