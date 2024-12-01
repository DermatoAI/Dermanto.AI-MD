package com.dermatoai.oauth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleAuthenticationService @Inject constructor(
    private val repository: GoogleAuthenticationRepository,
) {
    companion object {
        private const val TAG = "GoogleOauth2Service"
    }

    suspend fun doSignIn(
        context: Context,
        request: GetCredentialRequest,
        success: () -> Unit,
        error: (Throwable) -> Unit = {}
    ) {
        val credentialManager = CredentialManager.create(context)
        withContext(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignIn(result)
                success()
            } catch (e: GetCredentialCancellationException) {
                handleFailure(e)
                error(e)
            } catch (e: NoCredentialException) {
                handleFailure(e)
                error(e)
            } catch (e: GetCredentialException) {
                error(e)
            }
        }
    }

    private fun handleFailure(error: GetCredentialException) {
        Log.e(TAG, "Authentication ERROR: ${error.errorMessage}")
    }

    private suspend fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                val firebaseCredential =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val data = Firebase.auth.signInWithCredential(firebaseCredential).await()

                data.user?.let {
                    repository.saveToken(googleIdTokenCredential.idToken)
                    googleIdTokenCredential.profilePictureUri?.let { pp ->
                        repository.saveProfilePicture(pp)
                    }
                    it.displayName?.let { name -> repository.saveNickname(name) }
                    it.displayName?.let { name -> repository.saveAccountName(name) }
                }
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(TAG, "Received an invalid google id token response", e)
            }
        } else {
            Log.e(TAG, "Unexpected type of credential")
        }
    }
}