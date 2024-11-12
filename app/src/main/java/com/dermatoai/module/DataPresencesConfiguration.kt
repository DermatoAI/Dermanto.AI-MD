package com.dermatoai.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SecureDataPreferencesModule {

    @Singleton
    @Provides
    fun provideSecureDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.datastore

    companion object {
        private const val USER_PREFERENCES_NAME = "DermatoAI_preference"
        val Context.datastore: DataStore<Preferences> by preferencesDataStore(
            name = USER_PREFERENCES_NAME
        )
    }
}