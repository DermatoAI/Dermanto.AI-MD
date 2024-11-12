package com.dermatoai.media

import android.content.Context
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MediaModule @Inject constructor(@ApplicationContext private val context: Context) {
    @Provides
    @Singleton
    fun createCamera() = ProcessCameraProvider.getInstance(context)

    @Provides
    @Singleton
    fun createPhotoPicker() =
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
}