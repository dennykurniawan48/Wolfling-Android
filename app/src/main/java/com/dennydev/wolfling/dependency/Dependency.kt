package com.dennydev.wolfling.dependency

import android.app.Application
import android.content.Context
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@dagger.Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}

@dagger.Module
@InstallIn(SingletonComponent::class)
object ContextModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}