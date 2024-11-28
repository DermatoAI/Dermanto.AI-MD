package com.dermatoai.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.dermatoai.R
import com.dermatoai.databinding.ActivityResultBinding
import com.dermatoai.helper.ResultPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.toolbar
        toolbar.root.also {
            setTitle(R.string.result_header_title)
            setSupportActionBar(it)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.root.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val slideFragment = listOf(
            AnalyzeResultFragment(), AnalyzeChatAIFragment()
        )
        val slideName = listOf("Analyze Result", "AI Chat")

        binding.viewPageContainer.also {
            it.adapter = ResultPagerAdapter(this, slideFragment)
        }

        binding.tabLayoutView.also {
            TabLayoutMediator(it, binding.viewPageContainer) { tab, position ->
                tab.text = slideName[position]
            }.attach()
        }

        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@ResultActivity, BaseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}