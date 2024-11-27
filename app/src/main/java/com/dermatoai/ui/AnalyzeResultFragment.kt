package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dermatoai.databinding.FragmentAnalyzeResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyzeResultFragment : Fragment() {
    private lateinit var binding: FragmentAnalyzeResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyzeResultBinding.inflate(inflater, container, false)
        return binding.root
    }
}