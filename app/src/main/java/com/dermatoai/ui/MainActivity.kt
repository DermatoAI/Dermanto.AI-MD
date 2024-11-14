package com.dermatoai.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.GetCredentialRequest
import com.dermatoai.R
import com.dermatoai.databinding.ActivityMainBinding
import com.dermatoai.oauth.GoogleAuthenticationService
import com.dermatoai.oauth.GoogleIdOptionFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var googleAuthenticationService: GoogleAuthenticationService

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        googleAuthenticationService.doSignIn(
            GetCredentialRequest.Builder()
                .addCredentialOption(GoogleIdOptionFactory.SIGN_IN_ONLY).build()
        )
//
    }

//    private val browserActivityLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                val resp = result.data?.let { AuthorizationResponse.fromIntent(it) }
//                val ex = AuthorizationException.fromIntent(result.data)
//                if (resp != null) {
//                    val authState = AuthState(resp, ex)
//                    googleAuthenticationService.service()
//                        .performTokenRequest(resp.createTokenExchangeRequest()) { tokenResponse, exception ->
//                            authState.update(tokenResponse, exception)
//                        }
//                }
//            }
//        }
}