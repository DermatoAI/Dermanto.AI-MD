package com.dermatoai.ui

import android.os.Bundle
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
        toolbar.root.setTitle(R.string.result_header_title)

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
    }
}