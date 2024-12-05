package com.dermatoai.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dermatoai.databinding.ActivitySettingAndInfoBinding

class SettingAndInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivitySettingAndInfoBinding.inflate(layoutInflater)

        with(binding) {
            toolbar.root.also {
                setTitle("Setting And Information")
                setSupportActionBar(it)
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.root.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        setContentView(binding.root)
    }
}