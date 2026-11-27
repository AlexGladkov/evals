package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.models.ChatRequest
import com.dev.nicehash.data.providers.models.ChatResponse
import com.dev.nicehash.data.providers.services.ChatService
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Unit tests for the app-scoped chat repository: state persistence
 * (history accumulation, open/closed flag) and network error handling
 * (success / http error / timeout) via a fake ChatService.
 */
class ChatRepositoryTest {

    /** Fake ChatService with configurable behaviour (no Android deps) */
    private class StubChatService(private val behaviour: (ChatRequest) -> Single<ChatResponse>) : ChatService {
        override fun sendMessage(request: ChatRequest): Single<ChatResponse> = behaviour(request)
    }

    private fun echoService(): ChatService = StubChatService { request ->
        Single.just(ChatResponse(reply = request.text, ts = 1700000000L))
    }

    @Test
    fun `toggle changes open state`() {
        val repository = ChatRepositoryImpl(service = echoService())

        assertFalse(repository.isOpen.blockingFirst())
        repository.toggle()
        assertTrue(repository.isOpen.blockingFirst())
        repository.toggle()
        assertFalse(repository.isOpen.blockingFirst())
    }

    @Test
    fun `state survives navigation (read from a fresh screen)`() {
        val repository = ChatRepositoryImpl(service = echoService())
        repository.sendMessage(text = "hello").blockingGet()
        repository.toggle()

        // Simulates another screen reading the same app-scoped repository
        assertTrue(repository.isOpen.blockingFirst())
        assertEquals(2, repository.messages.size)
    }

    @Test
    fun `isOpen replays current state to late subscribers`() {
        val repository = ChatRepositoryImpl(service = echoService())
        val first = repository.isOpen.test()
        repository.toggle()
        val second = repository.isOpen.test()

        first.assertValues(false, true)
        second.assertValues(true)
    }

    @Test
    fun `messages accumulate across sends (history persists)`() {
        val repository = ChatRepositoryImpl(service = echoService())

        repository.sendMessage(text = "first").blockingGet()
        repository.sendMessage(text = "second").blockingGet()

        val messages = repository.messages
        assertEquals(4, messages.size)

        assertTrue(messages[0].isUser)
        assertEquals("first", messages[0].text)
        assertFalse(messages[1].isUser)
        assertEquals("first", messages[1].text) // echoed reply
        assertEquals(1700000000L, messages[1].timestamp)
        assertTrue(messages[2].isUser)
        assertEquals("second", messages[2].text)
        assertFalse(messages[3].isUser)
        assertEquals("second", messages[3].text)
    }

    @Test
    fun `http error surfaces and history stays intact`() {
        val failingService = StubChatService { _ ->
            Single.error(HttpException(Response.error<Any>(500,
                    ResponseBody.create(MediaType.parse("application/json"), "{}"))))
        }
        val repository = ChatRepositoryImpl(service = failingService)

        repository.sendMessage(text = "hello").test().assertError { it is HttpException }
        assertTrue(repository.messages.isEmpty())

        // UI maps failure into an error entry inside the chat (no crash)
        repository.addError(message = "not delivered")
        assertEquals(1, repository.messages.size)
        assertTrue(repository.messages[0].isError)
        assertFalse(repository.messages[0].isUser)
    }

    @Test
    fun `timeout error surfaces without crash`() {
        val timingOutService = StubChatService { _ -> Single.error(SocketTimeoutException("timeout")) }
        val repository = ChatRepositoryImpl(service = timingOutService)

        repository.sendMessage(text = "hello").test().assertError { it is SocketTimeoutException }
        assertTrue(repository.messages.isEmpty())
    }
}
