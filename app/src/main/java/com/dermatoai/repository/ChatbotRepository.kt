package com.dermatoai.repository

import com.dermatoai.api.ChatRequest
import com.dermatoai.api.DermatoEndpoint
import javax.inject.Inject

class ChatbotRepository @Inject constructor(private val apiService: DermatoEndpoint) {
    suspend fun requestChatbot(chatRequestBody: ChatRequest) = apiService.chatApi(chatRequestBody)
}