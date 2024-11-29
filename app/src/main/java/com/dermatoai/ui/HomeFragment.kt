package com.dermatoai.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.databinding.FragmentHomeBinding
import com.dermatoai.helper.HistoryListAdapter
import com.dermatoai.model.AnalyzeViewModel
import com.dermatoai.model.HomeViewModel
import com.dermatoai.oauth.OauthPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private val analyzeViewModel: AnalyzeViewModel by viewModels()

    @Inject
    lateinit var oauthPreferences: OauthPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val historyListAdapter = HistoryListAdapter()
        binding.historyRecycleView.adapter = historyListAdapter
        binding.historyRecycleView.layoutManager = LinearLayoutManager(requireContext())

        binding.settingButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                oauthPreferences.removeToken()
                oauthPreferences.getToken().collect {
                    if (it.isNullOrEmpty()) {
                        requireActivity().startActivity(
                            Intent(
                                binding.root.context,
                                LoginActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    }
                }
            }
        }

        homeViewModel.recordList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.historyRecycleView.visibility = GONE
                binding.historyEmptyAnima.visibility = VISIBLE
            } else {
                binding.historyRecycleView.visibility = VISIBLE
                binding.historyEmptyAnima.visibility = GONE
            }
            historyListAdapter.submitList(it)
        }

        analyzeViewModel.history.observe(viewLifecycleOwner) {
            homeViewModel.putRecordList(it)
        }
    }
}