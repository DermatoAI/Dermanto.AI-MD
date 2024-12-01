package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.databinding.FragmentAppointmentBinding
import com.dermatoai.helper.FinishedAppointmentListAdapter
import com.dermatoai.model.AppointmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    private lateinit var binding: FragmentAppointmentBinding
    private val viewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val finishedListAdapter = FinishedAppointmentListAdapter()
        binding.finishedContainerList.apply {
            adapter = finishedListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.historyAppointment.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                with(binding) {
                    finishedContainerList.visibility = GONE
                    historyEmptyAnima.visibility = VISIBLE
                }
            } else {
                with(binding){
                    finishedContainerList.visibility = VISIBLE
                    historyEmptyAnima.visibility = GONE
                }
                finishedListAdapter.submitList(it)
            }
        }
    }

}