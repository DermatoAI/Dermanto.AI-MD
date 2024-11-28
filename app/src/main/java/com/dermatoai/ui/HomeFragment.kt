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
import com.dermatoai.model.AnalyzeHistoryData
import com.dermatoai.model.HomeViewModel
import com.dermatoai.oauth.OauthPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var historyList: List<AnalyzeHistoryData>? = emptyList()
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

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

        viewModel.recordList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.historyRecycleView.visibility = GONE
                binding.historyEmptyAnima.visibility = VISIBLE
            } else {
                binding.historyRecycleView.visibility = VISIBLE
                binding.historyEmptyAnima.visibility = GONE
            }
            historyListAdapter.submitList(it)
        }

//        val historyList = listOf(
//            AnalyzeHistoryData(
//                id = "1",
//                date = Date(2023, 10, 20),
//                issue = "Melanoma",
//                score = 85
//            ),
//            AnalyzeHistoryData(
//                id = "2",
//                date = Date(2023, 11, 10),
//                issue = "Eczema",
//                score = 70
//            ),
//            AnalyzeHistoryData(
//                id = "3",
//                date = Date(2023, 12, 5),
//                issue = "Psoriasis",
//                score = 95
//            ),
//            AnalyzeHistoryData(
//                id = "4",
//                date = Date(2023, 12, 5),
//                issue = "Psoriasis",
//                score = 95
//            ),
//            AnalyzeHistoryData(
//                id = "5",
//                date = Date(2023, 12, 5),
//                issue = "Psoriasis",
//                score = 95
//            )
//        )
//        viewModel.putRecordList(historyList)
    }
}