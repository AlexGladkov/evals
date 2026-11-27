package com.dev.nicehash.domain.models

/**
 * Model for a single support chat message (domain level).
 * User messages are sent to the support backend; bot messages are echoes back.
 * [isError] marks local error entries (delivery failures) inside the chat history.
 */
data class ChatMessage(val text: String, val timestamp: Long, val isUser: Boolean,
                       val isError: Boolean = false)
