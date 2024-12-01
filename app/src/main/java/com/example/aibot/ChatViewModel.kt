package com.example.aibot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {
    val messageList by lazy{
        mutableStateListOf<MessageModel>()
    }
    val generavtiveModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey= Constant.apiKey

    )
    fun sendMessage(qustions: String) {
        viewModelScope.launch {
            try {
                val chat = generavtiveModel.startChat(
                    history = messageList.map {
                        content(it.role){text(it.message)}
                    }.toList()
                )
                messageList.add(MessageModel(qustions, "User"))
                messageList.add(MessageModel("Typing...", "Model"))


                val response = chat.sendMessage(qustions)
                messageList.removeAt(messageList.size-1)

                messageList.add(MessageModel(response.text.toString(), "Model"))

            }
            catch (e: Exception) {
                messageList.removeAt(messageList.size-1)
                messageList.add(MessageModel("Error: " + e.message.toString(), "Model"))
            }
        }
    }
}