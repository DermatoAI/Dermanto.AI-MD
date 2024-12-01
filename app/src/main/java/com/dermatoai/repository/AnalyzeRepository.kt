package com.dermatoai.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.dermatoai.api.DermatoEndpoint
import com.dermatoai.helper.networkBoundResource
import com.dermatoai.room.DiagnoseRecord
import com.dermatoai.room.DiagnoseRecordDAO
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AnalyzeRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val dao: DiagnoseRecordDAO,
    private val fetch: DermatoEndpoint,
) {
    fun analyzeImage(uri: Uri, userid: String) = networkBoundResource(
        query = {
            dao.getLatest(userid)
        },
        fetch = {
            val bitmapImage = resizeImage(uri, context)
            val file: File = bitmapToFile(bitmapImage, context)
            val requestFile =
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    file.asRequestBody("image/jpeg".toMediaType())
                )
            fetch.analyzeImage(requestFile)
        },
        saveFetchResult = { response ->
            response.result.apply {
                dao.add(
                    DiagnoseRecord(
                        0,
                        confidence,
                        diagnosis,
                        timestamp,
                        image,
                        treatmentSuggestions,
                        userid
                    )
                )
            }
        }
    )

    fun getAllAnalyzeRecord(userid: String) = dao.getAll(userid)

    private fun resizeImage(uri: Uri, context: Context): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        return Bitmap.createScaledBitmap(originalBitmap, 180, 180, true)
    }

    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
    }
}