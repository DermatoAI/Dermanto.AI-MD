package com.dermatoai.oauth

import android.content.Context
import android.content.Intent
import dagger.Module
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

@Module
class GoogleOauth2Service @Inject constructor(
    private val context: Context,
    private val repository: OauthRepository,
    private val authRequestBuilder: AuthorizationRequest.Builder
) {

    fun doAuthorization(): Intent {
        val service = AuthorizationService(context)
        return service.getAuthorizationRequestIntent(
            authRequestBuilder.setLoginHint("example@gmail.com").build()
        )
    }

    fun saveAuthState(authState: AuthState) {
        runBlocking {
            repository.saveAuthState(authState.jsonSerializeString())
        }
    }

    suspend fun getAuthState(): AuthState? {
        return repository.authState.firstOrNull()?.let { AuthState.jsonDeserialize(it) }
    }
}