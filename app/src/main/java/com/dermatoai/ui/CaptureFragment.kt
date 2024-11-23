package com.dermatoai.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dermatoai.databinding.FragmentCaptureBinding
import com.dermatoai.model.CaptureViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CaptureFragment : Fragment() {
    val viewModel: CaptureViewModel by viewModels()

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var binding: FragmentCaptureBinding
    private var permissionGranted = false
    private lateinit var previewView: PreviewView

    private lateinit var imageUri: Uri
    private lateinit var imageCapture: ImageCapture


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                viewModel.setImageUri(uri)
            }

        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                permissionGranted = isGranted
                if (isGranted) {
                    Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT)
                        .show()
                    cameraSetup()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCaptureBinding.inflate(inflater, container, false)
        previewView = binding.previewImage
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.imageCaptureUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                stopCamera()
                with(binding) {
                    previewImageCapture.apply {
                        setImageURI(uri)
                        visibility = VISIBLE
                    }
                    binding.previewImage.visibility = GONE
                }
            } ?: cameraSetup()
        }

        binding.captureButton.setOnClickListener {
            captureImage { uri ->
                viewModel.setImageUri(uri)
            }
        }

        binding.galleryButton.setOnClickListener {
            stopCamera()
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.resetButton.setOnClickListener {
            viewModel.setImageUri(null)
            with(binding) {
                previewImageCapture.visibility = GONE
                previewImage.visibility = VISIBLE
            }
            cameraSetup()
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            cameraSetup()
        }

    }

    private fun stopCamera() {
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll() // Stop all use cases
        }
    }

    private fun cameraSetup() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.surfaceProvider = previewView.surfaceProvider

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK) // Adjust for front-facing if needed
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong when preparing the camera",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("CAMERA SETUP ERROR", e.localizedMessage ?: "Unknown error")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun captureImage(captureHandler: (Uri?) -> Unit) {
        if (!::imageCapture.isInitialized) {
            Toast.makeText(requireContext(), "Camera is not ready", Toast.LENGTH_SHORT).show()
            return
        }

        val photoFile = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "DermatoAI-${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    imageUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    captureHandler(imageUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to capture photo: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    captureHandler(null)
                }
            }
        )
    }
}