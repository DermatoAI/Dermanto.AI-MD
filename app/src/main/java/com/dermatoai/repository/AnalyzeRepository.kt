package com.dermatoai.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.dermatoai.api.DermatoEndpoint
import com.dermatoai.genativeai.GeminiService
import com.dermatoai.helper.networkBoundResource
import com.dermatoai.room.DiagnoseRecord
import com.dermatoai.room.DiagnoseRecordDAO
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AnalyzeRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val dao: DiagnoseRecordDAO,
    private val fetch: DermatoEndpoint,
    private val geminiService: GeminiService,
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
            val userIdBody = userid.toRequestBody("text/plain".toMediaType())
            fetch.analyzeImage(requestFile, userIdBody)
        },
        saveFetchResult = { response ->
            response.apply {
                val additionalInfo: String =
                    geminiService.generateAdditionalInfo(data.diagnosis).text
                        ?: "No Additional Info can be found"
                dao.add(
                    with(response) {
                        DiagnoseRecord(
                            0,
                            confidenceScore = data.confidence.toInt(),
                            issue = data.diagnosis,
                            time = data.timestamp,
                            image = data.imageId,
                            additionalInfo = additionalInfo,
                            userId = data.userId
                        )
                    }
                )
            }
        }
    )

    fun getAllAnalyzeRecord(userid: String) = dao.getAll(userid)
    fun getById(id: Int, userid: String) = dao.getById(id, userid)

    private fun resizeImage(uri: Uri, context: Context): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(originalBitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(originalBitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(originalBitmap, 270f)
            else -> originalBitmap
        }

        return Bitmap.createBitmap(rotatedBitmap)
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val compressImage = compressToFileLimit(bitmap, 10)
        val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")

        val outputStream = FileOutputStream(file)
        outputStream.write(compressImage)
        outputStream.flush()
        outputStream.close()

        return file
    }

    fun compressToFileLimit(bitmap: Bitmap, maxSizeInMB: Int): ByteArray {
        val maxSizeInBytes = maxSizeInMB * 1024 * 1024
        var quality = 100
        var compressedData: ByteArray

        do {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            compressedData = outputStream.toByteArray()
            quality -= 5
        } while (compressedData.size > maxSizeInBytes && quality > 0)

        return compressedData
    }
}