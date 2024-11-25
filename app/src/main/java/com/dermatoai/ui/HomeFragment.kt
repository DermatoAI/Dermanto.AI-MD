package com.dermatoai.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.databinding.FragmentHomeBinding
import com.dermatoai.helper.HistoryListAdapter
import com.dermatoai.model.HistoryData
import com.dermatoai.oauth.OauthPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

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
                        binding.root.context.startActivity(
                            Intent(
                                binding.root.context,
                                LoginActivity::class.java
                            )
                        )
                    }
                }
            }
        }
        val historyList = listOf(
            HistoryData(
                id = "1",
                date = Date(2023, 10, 20),
                issue = "Melanoma",
                score = 85
            ),
            HistoryData(
                id = "2",
                date = Date(2023, 11, 10),
                issue = "Eczema",
                score = 70
            ),
            HistoryData(
                id = "3",
                date = Date(2023, 12, 5),
                issue = "Psoriasis",
                score = 95
            ),
            HistoryData(
                id = "4",
                date = Date(2023, 12, 5),
                issue = "Psoriasis",
                score = 95
            ),
            HistoryData(
                id = "5",
                date = Date(2023, 12, 5),
                issue = "Psoriasis",
                score = 95
            )
        )
        historyListAdapter.submitList(
            historyList
        )
    }
}