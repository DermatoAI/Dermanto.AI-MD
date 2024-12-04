package com.dermatoai.genativeai

import com.dermatoai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

class GeminiService {
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
        systemInstruction = content { text("i am a doctor in skin specialism,...") }
    )

    suspend fun generateAdditionalInfo(issue: String) = model.generateContent(issue)
}