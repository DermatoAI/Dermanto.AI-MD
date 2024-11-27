package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dermatoai.databinding.FragmentAnalyzeChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyzeChatAIFragment : Fragment() {
    private lateinit var binding: FragmentAnalyzeChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyzeChatBinding.inflate(inflater, container, false)
        return binding.root
    }
}