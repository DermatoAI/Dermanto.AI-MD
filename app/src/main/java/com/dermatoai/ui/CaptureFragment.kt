package com.dermatoai.ui

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.Surface
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
import com.dermatoai.DermatoAI.Companion.FROM_ACTIVITY
import com.dermatoai.R
import com.dermatoai.databinding.FragmentCaptureBinding
import com.dermatoai.model.CaptureViewModel
import com.dermatoai.ui.ResultActivity.Companion.IMAGE_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaptureFragment : Fragment() {
    private lateinit var binding: FragmentCaptureBinding

    @Volatile
    private var flashState = ImageCapture.FLASH_MODE_OFF
    private var cameraDirection: Int = CameraSelector.LENS_FACING_BACK

    private val captureViewModel: CaptureViewModel by viewModels()
    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var previewView: PreviewView

    private var imageUri: Uri? = null
    private var imageCapture: ImageCapture? = null

    private val orientationEventListener by lazy {
        object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                captureViewModel.setImageUri(uri)
            }

        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiBind()
        observerSection()
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


    private fun uiBind() {
        with(binding) {
            previewView = previewImage
            binding.resetButton.visibility = GONE

            captureButton.setOnClickListener {
                imageUri?.let {
                    val intent = Intent(requireActivity(), ResultActivity::class.java)
                    intent.putExtra(IMAGE_URL, it.toString())
                    intent.putExtra(FROM_ACTIVITY, "CAPTURE")
                    startActivity(intent)
                } ?: captureImage { uri ->
                    captureViewModel.setImageUri(uri)
                }

            }

            galleryButton.setOnClickListener {
                cameraStop()
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            resetButton.setOnClickListener {
                captureViewModel.setImageUri(null)
                with(binding) {
                    previewImageCapture.visibility = GONE
                    previewImage.visibility = VISIBLE
                    captureButton.setImageResource(0)
                }
                cameraSetup()
                imageUri = null
                resetButton.visibility = GONE
            }

            flashButton.setOnClickListener {
                captureViewModel.changeFlashState(flashState)
                cameraStop()
            }

            lensDirectionButton.setOnClickListener {
                captureViewModel.setLensState(cameraDirection)
            }
        }
    }

    private fun observerSection() {
        captureViewModel.imageCaptureUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                cameraStop()
                imageUri = uri
                with(binding) {
                    previewImageCapture.apply {
                        setImageURI(uri)
                        visibility = VISIBLE
                    }
                    binding.resetButton.visibility = VISIBLE
                    binding.previewImage.visibility = GONE
                }
                binding.captureButton.setImageResource(R.drawable.check_broken_icon)
            } ?: cameraSetup()
        }

        captureViewModel.flashState.observe(viewLifecycleOwner) { state ->
            with(binding) {
                when (state) {
                    ImageCapture.FLASH_MODE_OFF -> {
                        flashButton.setImageResource(R.drawable.icons8_flash_on)
                        flashState = ImageCapture.FLASH_MODE_ON
                    }

                    ImageCapture.FLASH_MODE_ON -> {
                        flashButton.setImageResource(R.drawable.icons8_flash_auto)
                        flashState = ImageCapture.FLASH_MODE_AUTO
                    }

                    ImageCapture.FLASH_MODE_AUTO -> {
                        flashButton.setImageResource(R.drawable.icons8_flash_off)
                        flashState = ImageCapture.FLASH_MODE_OFF
                    }
                }
            }
            cameraSetup()
        }

        captureViewModel.lensState.observe(viewLifecycleOwner) { state ->
            cameraDirection = if (state == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            cameraStop()
            cameraSetup()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageCapture?.targetRotation = previewView.display.rotation
    }

    private fun cameraStop() {
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
            orientationEventListener.disable()
        }
    }

    private fun cameraSetup() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        orientationEventListener.enable()

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.surfaceProvider = previewView.surfaceProvider

            imageCapture = ImageCapture.Builder()
                .setFlashMode(flashState)
                .setTargetRotation(previewView.display.rotation) // Set target rotation here
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraDirection)
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
        if (imageCapture == null) {
            Toast.makeText(requireContext(), "Camera is not ready", Toast.LENGTH_SHORT).show()
            return
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "DermatoAI-${System.currentTimeMillis()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/${getString(R.string.app_name)}")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val imageUri = outputFileResults.savedUri
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
