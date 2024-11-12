package com.dermatoai.oauth

import android.net.Uri
import com.dermatoai.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OauthModule {
    companion object {
        private const val CLIENT_ID: String = BuildConfig.CLIENT_ID

        /*
        https://idp.example.com/custom-scope
         */
        private const val SCOPE = "openid email profile"
        private val TOKEN_ENDPOINT = Uri.parse("https://oauth2.googleapis.com/token")
        private val AUTHENTICATION_ENDPOINT =
            Uri.parse("https://accounts.google.com/o/oauth2/v2/auth")
        private val REDIRECT_URI =
            Uri.parse("https://accounts.google.com/.well-known/openid-configuration")
    }

    @Provides
    @Singleton
    fun appAuthConfig(): AuthorizationServiceConfiguration =
        AuthorizationServiceConfiguration(AUTHENTICATION_ENDPOINT, TOKEN_ENDPOINT)


    /*
        client_id
        redirect_uri
        response_type
        scope
        code_challenge
        code_challenge_method
        state
        login_hint (op)
         */
    @Provides
    @Singleton
    fun createAuthorizationRequest(config: AuthorizationServiceConfiguration): AuthorizationRequest.Builder {
        return AuthorizationRequest.Builder(
            config,
            CLIENT_ID,
            AuthorizationRequest.CODE_CHALLENGE_METHOD_S256,
            REDIRECT_URI
        ).setScope(SCOPE)
    }
}