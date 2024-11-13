package com.dermatoai.media

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class CameraService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var imageUri: Uri
    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private var permissionGranted = false

    init {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.surfaceProvider = previewView.surfaceProvider

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            try {

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    context as LifecycleOwner, cameraSelector, preview, imageCapture
                )

            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "Something wrong when preparing the Camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun requestCameraPermission(activity: ComponentActivity, onPermissionResult: (Boolean) -> Unit) {
        val permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            permissionGranted = isGranted
            onPermissionResult(isGranted)
            if (!isGranted) {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun captureImage(captureHandler: (Uri?) -> Unit) {
        if (!::imageCapture.isInitialized) {
            Toast.makeText(context, "Camera is not ready", Toast.LENGTH_SHORT).show()
            return
        }

        val photoFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "DermatoAI-${System.currentTimeMillis()}.jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    imageUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    captureHandler(imageUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        context,
                        "Failed to capture photo: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    captureHandler(null)
                }
            }
        )
    }
}