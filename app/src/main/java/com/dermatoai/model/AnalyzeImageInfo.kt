package com.dermatoai.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AnalyzeImageInfo(
    val confidence: Int,
    val diagnosis: String,
    val timestamp: Date,
    val image: Uri,
    val treatmentSuggestions: String,
) : Parcelable