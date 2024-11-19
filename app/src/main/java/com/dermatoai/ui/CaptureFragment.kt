package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dermatoai.databinding.FragmentCaptureBinding


class CaptureFragment : Fragment() {
    private lateinit var binding: FragmentCaptureBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCaptureBinding.inflate(inflater, container, false)
        return binding.root
    }


}