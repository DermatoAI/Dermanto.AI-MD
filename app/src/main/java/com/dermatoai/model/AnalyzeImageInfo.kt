package com.dermatoai.model

import java.util.Date

data class AnalyzeImageInfo(
    val confidence: Int,
    val diagnosis: String,
    val timestamp: Date,
    val image: String,
    val treatmentSuggestions: String,
)