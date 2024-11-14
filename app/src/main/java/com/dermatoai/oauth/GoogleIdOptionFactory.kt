package com.dermatoai.oauth

import com.dermatoai.BuildConfig.WEB_CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import java.util.UUID

/*
 https://developer.android.com/identity/sign-in/credential-manager-siwg
 */
object GoogleIdOptionFactory {
    private val NONCE: String = UUID.randomUUID().toString()

    val WITH_AUTO_SELECT = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce(NONCE)
        .build()

    val SIGN_IN_ONLY = GetSignInWithGoogleOption.Builder("")
        .setNonce(NONCE)
        .build()
}