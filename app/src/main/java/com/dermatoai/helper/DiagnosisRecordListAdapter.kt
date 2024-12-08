package com.dermatoai.helper

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermatoai.R
import com.dermatoai.databinding.DiagnosisRecordCardBinding
import com.dermatoai.model.AnalyzeHistoryData
import com.dermatoai.ui.ResultActivity
import java.util.Locale

class DiagnosisRecordListAdapter :
    ListAdapter<AnalyzeHistoryData, DiagnosisRecordListAdapter.ViewHolder>(AnalyzeHistoryData.DIFF_UTIL) {

    private lateinit var binding: DiagnosisRecordCardBinding

    inner class ViewHolder(private val binding: DiagnosisRecordCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnalyzeHistoryData?) {
            item?.let {
                with(binding) {
                    historyDateText.text =
                        SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()).format(it.date)
                    historyIssueText.text = it.issue
                    historyAccurateText.text =
                        binding.root.context.getString(R.string.confidence_score_template, it.score)
                }
            }

            binding.root.setOnClickListener {
                Intent(binding.root.context, ResultActivity::class.java).apply {
                    putExtra(ResultActivity.DIAGNOSE_INFO, item?.id)
                    binding.root.context.startActivity(this)
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