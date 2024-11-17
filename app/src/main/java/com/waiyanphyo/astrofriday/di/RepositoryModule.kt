package com.waiyanphyo.astrofriday.di

import com.waiyanphyo.astrofriday.data.api.WeatherApiService
import com.waiyanphyo.astrofriday.data.repository.AstronomyRepository
import com.waiyanphyo.astrofriday.data.repository.AstronomyRepositoryImpl
import com.waiyanphyo.astrofriday.data.repository.SearchRepository
import com.waiyanphyo.astrofriday.data.repository.SearchRepositoryImpl
import com.waiyanphyo.astrofriday.data.repository.SportRepository
import com.waiyanphyo.astrofriday.data.repository.SportRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSearchRepository(apiService: WeatherApiService, @IoDispatcher ioDispatcher: CoroutineDispatcher): SearchRepository {
        return SearchRepositoryImpl(apiService, ioDispatcher)
    }

    @Provides
    fun provideSportRepository(apiService: WeatherApiService, @IoDispatcher ioDispatcher: CoroutineDispatcher): SportRepository {
        return SportRepositoryImpl(apiService, ioDispatcher)
    }

    @Provides
    fun provideAstronomyRepository(apiService: WeatherApiService, @IoDispatcher ioDispatcher: CoroutineDispatcher): AstronomyRepository {
        return AstronomyRepositoryImpl(apiService, ioDispatcher)
    }

}