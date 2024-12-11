package com.dermatoai.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dermatoai.R
import com.dermatoai.databinding.FragmentHomeBinding
import com.dermatoai.helper.DiagnosisRecordListAdapter
import com.dermatoai.helper.Resource
import com.dermatoai.model.AnalyzeViewModel
import com.dermatoai.model.HomeViewModel
import com.dermatoai.oauth.GoogleAuthenticationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private val analyzeViewModel: AnalyzeViewModel by viewModels()

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(500)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            if (location != null) {
                homeViewModel.putCurrentLocation(location)
                Log.d(
                    "Location",
                    "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                )
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
    }

    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestMultiplePermissions = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                Log.d("Permissions", "All permissions granted.")
                startLocationUpdates()
            } else {
                Log.e("Permissions", "Permission denied.")
            }
        }
    }

    @Inject
    lateinit var oauthPreferences: GoogleAuthenticationRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission()
        uiBind()
        observerSection()
    }

    private fun uiBind() {
        val historyListAdapter = DiagnosisRecordListAdapter()
        binding.historyRecycleView.adapter = historyListAdapter
        binding.historyRecycleView.layoutManager = LinearLayoutManager(requireContext())

        binding.settingAndInfoButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingAndInfoActivity::class.java)
            requireActivity().startActivity(intent)
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
    }

    private fun observerSection() {
        homeViewModel.currentLocation.observe(viewLifecycleOwner) { currentLocation ->
            currentLocation?.run {
                homeViewModel.requestClimateInfo(
                    latitude,
                    longitude
                )
            }
        }
        analyzeViewModel.history.observe(viewLifecycleOwner) {
            homeViewModel.putCurrentLocation(it)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            oauthPreferences.getProfilePicture().collect {
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.profileImage)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            oauthPreferences.getNickname().collect {
                binding.nicknameText.text = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            oauthPreferences.getAccountName().collect {
                binding.accountName.text = it
            }
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
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Log.d("Location", "Location updates started")
        } catch (e: SecurityException) {
            Log.e("Location Updates Error", "Failed to request location updates", e)
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            Log.d("Location", "Location updates stopped")
        } catch (e: Exception) {
            Log.e("Location", "Error while stopping location updates", e)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}