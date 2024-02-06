package com.jm.metrostationalert.di

import com.jm.metrostationalert.BuildConfig
import com.jm.metrostationalert.retrofit.OpenApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = BuildConfig.OpenAPIUrl

    @Singleton
    @Provides
    fun provideOpenApiService(): OpenApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(OpenApiService::class.java)
    }
}