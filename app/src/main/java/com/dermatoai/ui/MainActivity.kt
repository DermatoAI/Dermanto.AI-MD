package com.dermatoai.ui

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dermatoai.R
import com.dermatoai.databinding.ActivityMainBinding
import com.dermatoai.oauth.GoogleOauth2Service
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var googleOauth2Service: GoogleOauth2Service

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val intent = googleOauth2Service.doAuthorization()
        browserActivityLauncher.launch(intent)
    }

    private val browserActivityLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val resp = result.data?.let { AuthorizationResponse.fromIntent(it) }
                val ex = AuthorizationException.fromIntent(result.data)
                if (resp != null) {
                    val authState = AuthState(resp, ex)
                    googleOauth2Service.service()
                        .performTokenRequest(resp.createTokenExchangeRequest()) { tokenResponse, exception ->
                            authState.update(tokenResponse, exception)
                        }
                }
            }
        }
}