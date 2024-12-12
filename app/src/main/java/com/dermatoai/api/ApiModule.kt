package com.dermatoai.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun dermatoEndpoint1(): DermatoAnalyzeEndpoint = DermatoClient.analyzeService

    @Provides
    @Singleton
    fun dermatoEndpoint2(): DermatoBackendEndpoint = DermatoClient.backendService

    @Provides
    @Singleton
    fun dermatoEndpoint3(): DermatoChatBotEndpoint = DermatoClient.chatbotService

    @Provides
    @Singleton
    fun openMeteoEndpoint(): OpenMeteoEndpoint = OpenMeteoClient.service
}