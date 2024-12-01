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
    fun dermatoEndpoint(): DermatoEndpoint = DermatoClient.service

    @Provides
    @Singleton
    fun openMeteoEndpoint(): OpenMeteoEndpoint = OpenMeteoClient.service
}