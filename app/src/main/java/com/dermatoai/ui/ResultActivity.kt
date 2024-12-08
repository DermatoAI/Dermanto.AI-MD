package com.dermatoai.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dermatoai.DermatoAI.Companion.FROM_ACTIVITY
import com.dermatoai.R
import com.dermatoai.databinding.ActivityResultBinding
import com.dermatoai.helper.Resource
import com.dermatoai.helper.ResultPagerAdapter
import com.dermatoai.model.AnalyzeImageInfo
import com.dermatoai.model.AnalyzeViewModel
import com.dermatoai.model.ResultViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val analyzeViewModel: AnalyzeViewModel by viewModels()
    private val resultViewModel: ResultViewModel by viewModels()

    private val slideFragment = listOf(
        AnalyzeResultFragment(), AnalyzeChatAIFragment()
    )
    private val slideName = listOf("Analyze Result", "AI Chat")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrlString = intent.getStringExtra(IMAGE_URL)

        if (intent.getStringExtra(FROM_ACTIVITY) == "CAPTURE") {
            analyzeViewModel.currentAnalyzeImage(Uri.parse(imageUrlString))
                .observe(this) { result ->
                    when (result) {
                        is Resource.Error -> {
                            binding.analyzeLoadingAnima.visibility = GONE
                            AlertDialog.Builder(this)
                                .setTitle("Analyzer Error")
                                .setMessage(result.error?.message)
                                .setPositiveButton("OK") { _, _ ->
                                    onBackPressedDispatcher.onBackPressed()
                                }
                                .show()
                        }

                        is Resource.Loading -> {
                            binding.analyzeLoadingAnima.visibility = VISIBLE
                        }

                        is Resource.Success -> result.data?.run {
                            binding.analyzeLoadingAnima.visibility = GONE
                            resultViewModel.putResult(
                                AnalyzeImageInfo(
                                    confidenceScore,
                                    issue,
                                    time,
                                    Uri.parse(image),
                                    additionalInfo
                                )
                            )
                            successView()
                        }
                    }
                }
        } else {
            val intExtra = intent.getIntExtra(DIAGNOSE_INFO, -1)
            val info = analyzeViewModel.getAnalyzeResult(intExtra)
            info.observe(this) {
                it?.let {
                    resultViewModel.putResult(it)
                    successView()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun successView() {
        with(binding) {
            tabLayoutView.visibility = VISIBLE

            toolbar.root.also {
                setTitle(R.string.result_header_title)
                setSupportActionBar(it)
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.root.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            viewPageContainer.also {
                it.adapter = ResultPagerAdapter(this@ResultActivity, slideFragment)
            }

            tabLayoutView.also {
                TabLayoutMediator(it, binding.viewPageContainer) { tab, position ->
                    tab.text = slideName[position]
                }.attach()
            }
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@ResultActivity, BaseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
        const val DIAGNOSE_INFO = "DIAGNOSE_INFO"
    }
}