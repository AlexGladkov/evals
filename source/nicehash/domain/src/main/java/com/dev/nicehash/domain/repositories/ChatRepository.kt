package com.dev.nicehash.domain.repositories

import com.dev.nicehash.domain.models.ChatMessage
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Repository for support chat. App-scoped (singleton): history and
 * open/closed state survive navigation between screens.
 */
interface ChatRepository {

    /** Snapshot of the current chat history (user + bot + error entries) */
    val messages: List<ChatMessage>

    /** Stream of the open/closed state; replays the current value on subscribe */
    val isOpen: Observable<Boolean>

    /** Toggle the chat overlay open/closed */
    fun toggle()

    /** Send a user message; completes with the bot echo reply */
    fun sendMessage(text: String): Single<ChatMessage>

    /** Append a local error entry to the chat history (delivery failure) */
    fun addError(message: String)
}
