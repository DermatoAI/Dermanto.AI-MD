package com.dermatoai.oauth

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleAuthenticationRepository @Inject constructor(
    @ActivityContext private val context: Context,
    private val preferences: OauthPreferences
) {

    suspend fun saveToken(idToken: String) {
        withContext(Dispatchers.IO) {
            // TODO encrypt the token first
            preferences.saveToken(idToken)
        }
    }

    fun getToken(): Flow<String?> {
        // TODO decrypt the token after
        return preferences.getToken()
    }

    suspend fun saveUserId(id: String) {
        withContext(Dispatchers.IO) {
            preferences.saveUserId(id)
        }
    }

    fun saveUserId(): Flow<String?> {
        return preferences.getUserId()
    }

    suspend fun saveNickname(name: String) {
        withContext(Dispatchers.IO) {
            preferences.saveNickname(name)
        }
    }

    fun getNickname(): Flow<String?> {
        return preferences.getNickname()
    }

    suspend fun saveAccountName(name: String) {
        withContext(Dispatchers.IO) {
            preferences.saveAccountName(name)
        }
    }

    fun getAccountName(): Flow<String?> {
        return preferences.getAccountName()
    }

    suspend fun saveProfilePicture(uri: Uri) {
        withContext(Dispatchers.IO) {
            preferences.saveProfilePicture(uri.toString())
        }
    }

    fun getProfilePicture(): Flow<Uri> {
        return preferences.getProfilePicture().map { Uri.parse(it) }
    }

    suspend fun removeCredential() {
        preferences.removeCredential()
    }
}