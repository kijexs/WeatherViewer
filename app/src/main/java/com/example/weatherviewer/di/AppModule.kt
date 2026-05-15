package com.example.weatherviewer.di

import android.content.Context
import androidx.room.Room
import com.example.weatherviewer.data.local.WeatherDao
import com.example.weatherviewer.data.local.WeatherDatabase
import com.example.weatherviewer.data.remote.RetrofitInstance
import com.example.weatherviewer.data.remote.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext context: Context
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(): WeatherApiService {
        return RetrofitInstance.apiService
    }
}