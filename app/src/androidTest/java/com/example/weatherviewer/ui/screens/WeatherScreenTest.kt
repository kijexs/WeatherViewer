package com.example.weatherviewer.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.weatherviewer.data.model.CurrentWeather
import com.example.weatherviewer.data.model.DailyForecast
import com.example.weatherviewer.data.model.HourlyForecast
import com.example.weatherviewer.data.model.WeatherForecast
import com.example.weatherviewer.data.model.toDayWithWeekday
import com.example.weatherviewer.data.model.toHourDisplay
import com.example.weatherviewer.data.repository.WeatherRepository
import com.example.weatherviewer.ui.screen.WeatherScreen
import com.example.weatherviewer.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

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
        fun createFakeForecast(): WeatherForecast {
            val now = java.time.LocalDateTime.now()
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00")
            val todayStr = now.toLocalDate().toString()

            val hours = (0..5).map { offset ->
                val time = now.plusHours(offset.toLong())
                HourlyForecast(
                    time = time.format(formatter),
                    temperature = (10 + offset * 2).toDouble(),
                    weatherCode = 0
                )
            }
            return WeatherForecast(
                current = CurrentWeather(22.0, 0, 5.0),
                daily = listOf(DailyForecast(todayStr, 25.0, 15.0, 0)),
                hourly = hours
            )
        }
    }

    @Test
    fun displaysCurrentWeatherOnSuccess() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(createFakeForecast())

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("22.0°C").assertIsDisplayed()
        composeTestRule
            .onAllNodesWithText("ясно")
            .apply { get(0).assertExists() }
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
        val forecast = createFakeForecast()
        val firstHourDisplay = forecast.hourly[0].time.toHourDisplay()

        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(forecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        val dayText = forecast.daily[0].date.toDayWithWeekday()
        composeTestRule.onNodeWithText(dayText).performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(firstHourDisplay).assertIsDisplayed()
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
        val forecast = createFakeForecast()
        val firstHour = forecast.hourly[1]
        val firstHourDisplay = firstHour.time.toHourDisplay()
        val firstTemp = "${firstHour.temperature.toInt()}°C"

        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(forecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Почасовой прогноз").assertExists()
        composeTestRule.onNodeWithText(firstHourDisplay).assertExists()
        composeTestRule.onNodeWithText(firstTemp).assertExists()
    }

    @Test
    fun displaysDailyForecast() = runTest {
        val forecast = createFakeForecast()

        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(forecast)

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Прогноз на 7 дней").assertIsDisplayed()
        val dayText = forecast.daily[0].date.toDayWithWeekday()
        composeTestRule.onNodeWithText(dayText).assertIsDisplayed()
    }

    @Test
    fun displaysCityName() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(createFakeForecast())

        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WeatherScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Петергоф").assertIsDisplayed()
    }

    @Test
    fun retryButtonNotShownOnSuccess() = runTest {
        coEvery { repository.getWeatherForecast(any(), any()) } returns flowOf(createFakeForecast())
        viewModel = WeatherViewModel(repository)

        composeTestRule.setContent { WeatherScreen(viewModel = viewModel) }

        composeTestRule.onNodeWithText("Повторить").assertDoesNotExist()
    }
}
