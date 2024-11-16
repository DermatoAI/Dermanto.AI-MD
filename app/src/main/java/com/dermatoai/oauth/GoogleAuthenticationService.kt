package com.dermatoai.oauth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.dermatoai.BuildConfig
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class GoogleAuthenticationService @Inject constructor(
    private val repository: GoogleAuthenticationRepository,
    private val credentialManager: CredentialManager
) {
    companion object {
        private const val TAG = "GoogleOauth2Service"
    }

    suspend fun doSignIn(context: Context, request: GetCredentialRequest) {
        withContext(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                handleFailure(e)
            }
        }
    }

    private fun handleFailure(error: GetCredentialException) {
        println(error.toString())
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        val payload: GoogleIdToken.Payload? =
                            verifyGoogleIdToken(googleIdTokenCredential.idToken)
                        payload?.let {
                            with(googleIdTokenCredential) {
                                val username = displayName
                                val profilePicture = profilePictureUri
                                val email = it.email
                                repository.saveToken(idToken)
                            }
                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun verifyGoogleIdToken(idTokenString: String): GoogleIdToken.Payload? {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(listOf("${BuildConfig.CLIENT_ID}.apps.googleusercontent.com")) // Replace with your client ID
            .build()

        try {
            val idToken: GoogleIdToken? = verifier.verify(idTokenString)
            if (idToken != null) {
                return idToken.payload
            } else {
                println("Invalid ID token.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}