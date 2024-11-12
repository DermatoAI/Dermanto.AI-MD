package com.dermatoai.oauth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OauthRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private val authStateField = stringPreferencesKey("oauth_state")

    val authState
        get() = dataStore.data.map {
            it[authStateField]
        }

    suspend fun saveAuthState(token: String) {
        dataStore.edit {
            it[this.authStateField] = token
        }
    }

}