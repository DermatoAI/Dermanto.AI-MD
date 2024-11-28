package com.dermatoai.model

/**
 * used for climate information data structure in HomeFragment.
 * @param sunType
 * @param temperature
 * @param humidity
 * @param force
 * @param uvScale
 *
 * @property sunType
 * @property temperature
 * @property humidity
 * @property force
 * @property uvScale
 */
data class ClimateInfoData(
    val sunType: String,
    val temperature: Int,
    val humidity: Int,
    val force: Int,
    val uvScale: Int,
)
