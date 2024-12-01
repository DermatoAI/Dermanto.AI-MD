package com.dermatoai.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.R
import com.dermatoai.databinding.FragmentHomeBinding
import com.dermatoai.helper.HistoryListAdapter
import com.dermatoai.helper.Resource
import com.dermatoai.model.AnalyzeViewModel
import com.dermatoai.model.HomeViewModel
import com.dermatoai.oauth.OauthPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private val analyzeViewModel: AnalyzeViewModel by viewModels()

    @Inject
    lateinit var oauthPreferences: OauthPreferences

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
        checkLocationPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val historyListAdapter = HistoryListAdapter()
        binding.historyRecycleView.adapter = historyListAdapter
        binding.historyRecycleView.layoutManager = LinearLayoutManager(requireContext())

        binding.settingButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                oauthPreferences.removeToken()
                oauthPreferences.getToken().collect {
                    if (it.isNullOrEmpty()) {
                        requireActivity().startActivity(
                            Intent(
                                binding.root.context,
                                LoginActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    }
                }
            }
        }

        homeViewModel.recordList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.historyRecycleView.visibility = GONE
                binding.historyEmptyAnima.visibility = VISIBLE
            } else {
                binding.historyRecycleView.visibility = VISIBLE
                binding.historyEmptyAnima.visibility = GONE
            }
            historyListAdapter.submitList(it)
        }

        analyzeViewModel.history.observe(viewLifecycleOwner) {
            homeViewModel.putRecordList(it)
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                homeViewModel.requestClimateInfo(latitude, longitude)
                Log.d("Location", "Lat: $latitude, Lon: $longitude")
            } else {
                Log.e("Location", "Fail to get location")
            }
        }.addOnFailureListener {
            Log.e("Location", "Error: ${it.message}")
        }

        homeViewModel.climateInfo.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> Unit
                is Resource.Loading -> Unit
                is Resource.Success -> binding.apply {
                    resource.data?.run {
                        temperature.let {
                            temperatureNumberMini.text = getString(R.string.celsius_format, it)
                            temperatureNumberText.text = getString(R.string.degree_format, it)
                        }
                        force.let { windNumberMini.text = getString(R.string.speed_format, it) }
                        humidity.let {
                            humidityNumberMini.text = getString(R.string.percent_format, it)
                        }
                        uvi.let { uvNumberText.text = it.toString() }
                        cloudCategory.let { cloudStatusText.text = it }
                        cloudDescription.let { cloudDescriptionText.text = it }
                        cloudIcon.let { cloudIconIndicator.setImageResource(it) }
                    }
                }
            }

        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}