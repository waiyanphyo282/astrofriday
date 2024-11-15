package com.waiyanphyo.astrofriday.di

import com.waiyanphyo.astrofriday.data.api.WeatherApiService
import com.waiyanphyo.astrofriday.data.repository.SearchRepository
import com.waiyanphyo.astrofriday.data.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSearchRepository(apiService: WeatherApiService, ioDispatcher: CoroutineDispatcher): SearchRepository {
        return SearchRepositoryImpl(apiService, ioDispatcher)
    }

}