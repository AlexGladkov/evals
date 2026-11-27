package com.dev.nicehash.data.providers.services

import com.dev.nicehash.data.providers.models.ChatRequest
import com.dev.nicehash.data.providers.models.ChatResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API calls for the support chat (echo server)
 */
interface ChatService {

    @POST("chat/message")
    fun sendMessage(@Body request: ChatRequest): Single<ChatResponse>
}
