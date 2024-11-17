package com.dermatoai.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dermatoai.databinding.ActivityCaptureBinding
import com.dermatoai.media.CameraService
import com.dermatoai.media.GalleryService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CaptureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCaptureBinding

    @Inject
    lateinit var galleryService: GalleryService

    @Inject
    lateinit var cameraService: CameraService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener {
            galleryService.registerPhotoPicker(this) {
                println(it.toString())
            }
            galleryService.openPhotoPicker()
        }

        cameraService.setup(binding.previewImage)
        cameraService.requestCameraPermission(this) {
        }

        binding.cameraButton.setOnClickListener {
            cameraService.captureImage {
                Log.i("ANJING", it.toString())
            }
        }
    }
}