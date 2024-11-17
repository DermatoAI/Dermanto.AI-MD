package com.dermatoai.media

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class GalleryService @Inject constructor() {
    private lateinit var pickVisualMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    fun registerPhotoPicker(activity: ComponentActivity, imageHandler: (Uri?) -> Unit) {
        pickVisualMediaLauncher =
            activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                uri?.let {
                    imageHandler(it)
                    return@registerForActivityResult
                }
                imageHandler(null)
            }
    }

    fun openPhotoPicker() {
        val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        pickVisualMediaLauncher.launch(request)
    }
}