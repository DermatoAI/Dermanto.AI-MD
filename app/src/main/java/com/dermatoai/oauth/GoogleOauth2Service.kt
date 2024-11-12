package com.dermatoai.oauth

import android.content.Context
import android.content.Intent
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

@Module
@InstallIn(ServiceComponent::class)
class GoogleOauth2Service @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: OauthRepository,
    private val authRequestBuilder: AuthorizationRequest.Builder
) {

    private val authorizationService = AuthorizationService(context)

    fun doAuthorization(): Intent {
        val service = authorizationService
        return service.getAuthorizationRequestIntent(
            authRequestBuilder.setLoginHint("example@gmail.com").build()
        )
    }

    fun service() = authorizationService

    fun saveAuthState(authState: AuthState) {
        runBlocking {
            repository.saveAuthState(authState.jsonSerializeString())
        }
    }

    suspend fun getAuthState(): AuthState? {
        return repository.authState.firstOrNull()?.let { AuthState.jsonDeserialize(it) }
    }
}