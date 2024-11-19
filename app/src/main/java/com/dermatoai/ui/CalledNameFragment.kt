package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dermatoai.databinding.FragmentCalledNameBinding

class CalledNameFragment : Fragment() {
    private lateinit var binding: FragmentCalledNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalledNameBinding.inflate(inflater, container, false)
        return binding.root
    }

}