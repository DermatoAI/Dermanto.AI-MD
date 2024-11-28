package com.dermatoai.helper

import android.icu.text.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermatoai.R
import com.dermatoai.databinding.DiagnosisRecordCardBinding
import com.dermatoai.model.AnalyzeHistoryData

class HistoryListAdapter :
    ListAdapter<AnalyzeHistoryData, HistoryListAdapter.ViewHolder>(AnalyzeHistoryData.DIFF_UTIL) {

    private lateinit var binding: DiagnosisRecordCardBinding

    inner class ViewHolder(private val binding: DiagnosisRecordCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val defaultDateFormat = DateFormat.getInstance()
        fun bind(item: AnalyzeHistoryData?) {
            item?.let {
                with(binding) {
                    historyDateText.text = defaultDateFormat.format(it.date)
                    historyIssueText.text = it.issue
                    historyAccurateText.text =
                        binding.root.context.getString(R.string.confidence_score_template, it.score)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            DiagnosisRecordCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}