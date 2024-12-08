package com.dermatoai.genativeai

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AIModule {

    @Provides
    fun provideAnalyzeViewModel(@ApplicationContext context: Context): GeminiService {
        return GeminiService(context)
    }

}