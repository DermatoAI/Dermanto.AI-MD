package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dermatoai.api.ChatRequest
import com.dermatoai.helper.ChatData
import com.dermatoai.repository.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatAiViewModel @Inject constructor(
    private val repository: ChatbotRepository
) : ViewModel() {
    private val _chatMessages = MutableLiveData<MutableList<ChatData>>()
    val chatMessages: LiveData<MutableList<ChatData>> get() = _chatMessages

    fun addMessage(message: ChatData) {
        val currentList = _chatMessages.value ?: mutableListOf()
        val updatedList = currentList.toMutableList() // Create a new instance
        updatedList.add(message)
        _chatMessages.value = updatedList // Assign the new list
    }

    fun clearMessages() {
        _chatMessages.value = mutableListOf()
    }

    suspend fun requestChatbot(senderMessage: String) {
        try {

            repository.requestChatbot(ChatRequest(senderMessage)).also { response ->
                response.message?.let {
                    withContext(Dispatchers.Main) {
                        addMessage(ChatData(it, false, Date()))
                    }
                }
                response.error?.let {
                    withContext(Dispatchers.Main) {

                        addMessage(ChatData(it, false, Date()))
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                addMessage(ChatData("Something went wrong \nplease try again", false, Date()))
            }
        }
    }
}
