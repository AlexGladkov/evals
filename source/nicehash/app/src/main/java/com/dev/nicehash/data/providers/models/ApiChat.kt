package com.dev.nicehash.data.providers.models

/**
 * Request body for POST /chat/message
 */
data class ChatRequest(val text: String)

/**
 * Response body for POST /chat/message (echo server: reply = text, ts = unix timestamp)
 */
data class ChatResponse(val reply: String, val ts: Long)
