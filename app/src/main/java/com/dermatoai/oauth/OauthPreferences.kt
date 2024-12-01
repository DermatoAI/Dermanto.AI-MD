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
    private val userId = stringPreferencesKey("userId")
    private val profilePicture = stringPreferencesKey("profile_picture_url")
    private val nickname = stringPreferencesKey("nickname")
    private val accountName = stringPreferencesKey("account_name")

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

    fun getUserId(): Flow<String?> {
        return dataStore.data.map {
            it[this.userId]
        }
    }

    suspend fun saveUserId(id: String) {
        dataStore.edit {
            it[this.userId] = id
        }
    }

    fun getNickname(): Flow<String?> {
        return dataStore.data.map {
            it[this.nickname]
        }
    }

    suspend fun saveNickname(name: String) {
        dataStore.edit {
            it[this.nickname] = name
        }
    }

    fun getAccountName(): Flow<String?> {
        return dataStore.data.map {
            it[this.accountName]
        }
    }

    suspend fun saveAccountName(name: String) {
        dataStore.edit {
            it[this.accountName] = name
        }
    }

    fun getProfilePicture(): Flow<String?> {
        return dataStore.data.map {
            it[this.profilePicture]
        }
    }

    suspend fun saveProfilePicture(url: String) {
        dataStore.edit {
            it[this.profilePicture] = url
        }
    }


    suspend fun removeCredential() {
        dataStore.edit {
            it[this.token] = ""
            it[this.profilePicture] = ""
            it[this.nickname] = ""
            it[this.accountName] = ""
        }
    }


}