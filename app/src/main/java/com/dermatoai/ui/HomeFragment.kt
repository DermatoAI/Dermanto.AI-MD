package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.databinding.FragmentHomeBinding
import com.dermatoai.helper.HistoryListAdapter
import com.dermatoai.model.HistoryData
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

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