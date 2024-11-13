package com.dermatoai.media

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class GalleryService @Inject constructor(@ApplicationContext context: Context) {
    private lateinit var pickVisualMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    fun registerPhotoPicker(activity: ComponentActivity, imageHandler: (Uri?) -> Unit) {
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