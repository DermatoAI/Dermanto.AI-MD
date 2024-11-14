package com.dermatoai.oauth

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GoogleAuthenticationRepository @Inject constructor(
    @ActivityContext private val context: Context,
    private val preferences: OauthPreferences
) {

    fun saveToken(idToken: String) {
        runBlocking {
            // TODO encrypt the token first
            preferences.saveToken(idToken)
        }
    }

    fun getToken(): Flow<String?> {
        // TODO decrypt the token after
        return preferences.getToken()
    }
}