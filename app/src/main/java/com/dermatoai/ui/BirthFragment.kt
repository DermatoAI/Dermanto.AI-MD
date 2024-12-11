package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dermatoai.R
import com.dermatoai.databinding.FragmentBirthBinding
import java.util.Calendar
import java.util.Locale


class BirthFragment : Fragment() {
    private lateinit var binding: FragmentBirthBinding

    private val args: BirthFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBirthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val months = resources.getStringArray(R.array.months)
        ArrayAdapter(
            requireContext(),
            R.layout.costume_drop_down_list,
            months
        ).also { adapter ->
            binding.monthInputText.setAdapter(adapter)
        }

        binding.continueButton.setOnClickListener {
            val dateText = binding.dateInputText.text.toString()
            val monthText = binding.monthInputText.text.toString()
            val yearText = binding.yearInputText.text.toString()

            val birthDate = Calendar.getInstance(Locale.getDefault()).apply {
                set(Calendar.DAY_OF_MONTH, dateText.toInt())
                set(Calendar.MONTH, months.indexOf(monthText))
                set(Calendar.YEAR, yearText.toInt())
            }

            it.findNavController()
                .navigate(
                    BirthFragmentDirections.actionBirthFragmentToCalledNameFragment(
                        args.userId,
                        birthDate.timeInMillis
                    )
                )
        }
    }
}