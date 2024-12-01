package com.dermatoai.model

/**
 * used for climate information data structure in HomeFragment.
 * @param cloudCategory
 * @param cloudDescription
 * @param temperature
 * @param humidity
 * @param force
 * @param uvi
 *
 * @property cloudCategory
 * @property cloudDescription
 * @property temperature
 * @property humidity
 * @property force
 * @property uvi
 */
data class ClimateInfoData(
    val cloudCategory: String,
    val cloudDescription: String,
    val cloudIcon: Int,
    val temperature: Double,
    val humidity: Int,
    val force: Double,
    val uvi: Double,
)
