package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.dermatoai.databinding.FragmentAnalyzeResultBinding
import com.dermatoai.model.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class AnalyzeResultFragment : Fragment() {
    private lateinit var binding: FragmentAnalyzeResultBinding

    private val resultViewModel: ResultViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val markwon = Markwon.create(requireContext())
        binding = FragmentAnalyzeResultBinding.inflate(inflater, container, false)
        resultViewModel.successResult.observe(viewLifecycleOwner) {
            with(binding) {
                resultClassifyText.text = it.diagnosis
                markwon.setMarkdown(resultAdditionalInformationText, it.treatmentSuggestions)
                Glide.with(requireActivity())
                    .load(it.image)
                    .into(resultImage)
            }
        }
        return binding.root
    }
}