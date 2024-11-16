package com.dermatoai.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.GetCredentialRequest
import com.dermatoai.BuildConfig
import com.dermatoai.databinding.ActivitySignInBinding
import com.dermatoai.oauth.GoogleAuthenticationService
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var googleSignInService: GoogleAuthenticationService

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.googleSignInButton.setOnClickListener {

            val rawNonce = UUID.randomUUID().toString()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(rawNonce.toByteArray())
            val hashedNonce = digest.fold("") { s: String, byte: Byte ->
                s.format("%02x", byte)
            }

            val googleOption = GetSignInWithGoogleOption.Builder(BuildConfig.SERVER_CLIENT_ID)
                .setNonce(hashedNonce)
                .build()

            CoroutineScope(Dispatchers.Default).launch {
                googleSignInService
                    .doSignIn(
                        binding.root.context,
                        GetCredentialRequest.Builder()
                            .addCredentialOption(googleOption)
                            .build()
                    )
            }
        }

    }
}