package com.dermatoai.model

data class ClimateInfoData(
    val sunType: String,
    val temperature: Int,
    val humidity: Int,
    val force: Int,
    val uvScale: Int,
)
