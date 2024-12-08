package com.dermatoai.genativeai

import android.content.Context
import android.util.Log
import com.dermatoai.BuildConfig
import com.dermatoai.R
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GeminiService @Inject constructor(
    @ApplicationContext context: Context
) {
    private val model: GenerativeModel = GenerativeModel(
        modelName = BuildConfig.GEMINI_MODEL_TYPE,
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
        systemInstruction = content { text(context.getString(R.string.gemini_system_instruction)) }
    )

    suspend fun generateAdditionalInfo(issue: String): GenerateContentResponse {
        return try {
            model.generateContent(issue)
        } catch (e: Exception) {
            Log.e("GeminiService", "Error generating content", e)
            throw e
        }
    }
}