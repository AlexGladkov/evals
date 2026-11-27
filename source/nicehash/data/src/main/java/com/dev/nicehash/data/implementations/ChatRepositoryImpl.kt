package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.models.ChatRequest
import com.dev.nicehash.data.providers.services.ChatService
import com.dev.nicehash.domain.models.ChatMessage
import com.dev.nicehash.domain.repositories.ChatRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Chat repository implementation. App-scoped singleton provided by Dagger:
 * history and open/closed state survive navigation between screens.
 */
@Singleton
class ChatRepositoryImpl @Inject constructor(private val service: ChatService) : ChatRepository {

    private val _messages = mutableListOf<ChatMessage>()
    private val _isOpen = BehaviorSubject.createDefault(false)

    override val messages: List<ChatMessage>
        get() = synchronized(_messages) { _messages.toList() }

    override val isOpen: Observable<Boolean> = _isOpen

    override fun toggle() {
        _isOpen.onNext(!(_isOpen.value ?: false))
    }

    override fun sendMessage(text: String): Single<ChatMessage> {
        return service.sendMessage(ChatRequest(text = text))
                .map { ChatMessage(text = it.reply, timestamp = it.ts, isUser = false) }
                .doOnSuccess { msg ->
                    addMessage(ChatMessage(text = text, timestamp = System.currentTimeMillis() / 1000, isUser = true))
                    addMessage(msg)
                }
    }

    override fun addError(message: String) {
        addMessage(ChatMessage(text = message, timestamp = System.currentTimeMillis() / 1000,
                isUser = false, isError = true))
    }

    private fun addMessage(message: ChatMessage) {
        synchronized(_messages) { _messages.add(message) }
    }
}
