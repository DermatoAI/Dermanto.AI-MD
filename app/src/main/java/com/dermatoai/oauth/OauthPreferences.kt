package com.dermatoai.oauth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OauthPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private val token = stringPreferencesKey("oauth_state")

    fun getToken(): Flow<String?> {
       return dataStore.data.map {
            it[this.token]
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[this.token] = token
        }
    }

}