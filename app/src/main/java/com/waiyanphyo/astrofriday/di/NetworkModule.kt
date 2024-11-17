package com.waiyanphyo.astrofriday.di

import com.squareup.moshi.Moshi
import com.waiyanphyo.astrofriday.BuildConfig
import com.waiyanphyo.astrofriday.data.api.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)


        // Add an interceptor to add the API key as a query parameter to all requests
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            // Check for the presence of the "No-Api-Key" header
            if (originalRequest.header("No-Api-Key") == null) {
                // Add the API key as a query parameter if the header is absent
                val urlWithApiKey = originalUrl.newBuilder()
                    .addQueryParameter("key", BuildConfig.WEATHER_API_KEY)
                    .build()

                // Create a new request with the updated URL
                val requestWithApiKey = originalRequest.newBuilder()
                    .url(urlWithApiKey)
                    .build()

                chain.proceed(requestWithApiKey)
            } else {
                // If the "No-Api-Key" header is present, proceed with the original request
                chain.proceed(originalRequest)
            }
        }
        clientBuilder.addInterceptor(apiKeyInterceptor)

        // Add a logging interceptor for debugging
        if (BuildConfig.DEBUG) {
            clientBuilder
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        val moshi = Moshi.Builder()
            .build()

        // Create the Retrofit instance
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .client(clientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

}