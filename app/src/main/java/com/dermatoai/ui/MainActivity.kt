package com.dermatoai.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dermatoai.oauth.OauthPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var oauthPreferences: OauthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            oauthPreferences.getToken().collect {
                if (it.isNullOrEmpty()) {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this@MainActivity, BaseActivity::class.java))
                }
            }
        }
    }
}