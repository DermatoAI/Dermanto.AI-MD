package com.dermatoai.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dermatoai.databinding.ActivitySettingAndInfoBinding
import com.dermatoai.oauth.OauthPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingAndInfoActivity : AppCompatActivity() {

    @Inject
    lateinit var oauthPreferences: OauthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingAndInfoBinding.inflate(layoutInflater)

        with(binding) {
            toolbar.root.also {
                setTitle("Setting And Information")
                setSupportActionBar(it)
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.root.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            logoutButton.setOnClickListener {
                lifecycleScope.launch {
                    oauthPreferences.removeCredential()
                    oauthPreferences.getToken().collect { it ->
                        if (it.isNullOrEmpty()) {
                            val intent = Intent(
                                this@SettingAndInfoActivity,
                                MainActivity::class.java
                            )
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(
                                intent
                            )
                            finish()
                        }
                    }
                }
            }
        }
        setContentView(binding.root)
    }
}