package com.dermatoai.repository

import com.dermatoai.api.ChatRequest
import com.dermatoai.api.DermatoChatBotEndpoint
import javax.inject.Inject

class ChatbotRepository @Inject constructor(private val apiService: DermatoChatBotEndpoint) {
    suspend fun requestChatbot(chatRequestBody: ChatRequest) = apiService.chatApi(chatRequestBody)
}