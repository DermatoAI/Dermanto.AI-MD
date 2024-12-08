package com.dermatoai

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * A class representing the main application entry point for DermatoAI.
 *
 * This class is annotated with `@HiltAndroidApp`, enabling Hilt for dependency injection throughout the app.
 */
@HiltAndroidApp
class DermatoAI : Application() {
    companion object {
        const val FROM_ACTIVITY = "FROM_ACTIVITY"
    }
}