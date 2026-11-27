package com.dev.nicehash.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.dev.nicehash.R
import com.dev.nicehash.app.adapters.ChatAdapter
import com.dev.nicehash.domain.models.ChatMessage
import com.dev.nicehash.domain.repositories.ChatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * Global support chat overlay (FAB + panel). Inflated by BaseActivity on top of
 * every screen's content; the state itself lives in the app-scoped ChatRepository,
 * so history and open/closed state survive navigation between screens.
 */
class ChatOverlayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val chatAdapter = ChatAdapter()

    private lateinit var panelChat: View
    private lateinit var btnToggle: FloatingActionButton
    private lateinit var listChat: RecyclerView
    private lateinit var inputChat: EditText

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_chat_overlay, this, true)

        panelChat = findViewById(R.id.layout_chat_panel)
        btnToggle = findViewById(R.id.btn_chat_toggle)
        listChat = findViewById(R.id.list_chat)
        inputChat = findViewById(R.id.input_chat)

        listChat.layoutManager = LinearLayoutManager(context)
        listChat.adapter = chatAdapter
    }

    /**
     * Wire the overlay to the shared chat repository. Subscriptions are added
     * to the passed activity-scoped [disposables] and cleaned up on destroy.
     */
    fun bind(repository: ChatRepository, disposables: CompositeDisposable) {
        btnToggle.setOnClickListener { repository.toggle() }
        findViewById<View>(R.id.btn_chat_close).setOnClickListener { repository.toggle() }
        findViewById<View>(R.id.btn_chat_send).setOnClickListener {
            sendChatMessage(repository = repository, disposables = disposables)
        }

        disposables.add(repository.isOpen
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ open -> onOpenChanged(open = open, repository = repository) },
                        { error -> Timber.e(error, "Chat state subscription failed") }))
    }

    private fun onOpenChanged(open: Boolean, repository: ChatRepository) {
        panelChat.visibility = if (open) View.VISIBLE else View.GONE
        btnToggle.visibility = if (open) View.GONE else View.VISIBLE
        if (open) showMessages(messages = repository.messages)
    }

    private fun sendChatMessage(repository: ChatRepository, disposables: CompositeDisposable) {
        val text = inputChat.text?.toString()?.trim().orEmpty()
        if (text.isEmpty()) return

        inputChat.setText("")

        disposables.add(repository.sendMessage(text = text)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ showMessages(messages = repository.messages) }, { error ->
                    Timber.e(error, "Chat message delivery failed")
                    // Error state lives in the chat itself, never crashes the app
                    repository.addError(message = context.getString(R.string.chat_error))
                    showMessages(messages = repository.messages)
                }))
    }

    private fun showMessages(messages: List<ChatMessage>) {
        chatAdapter.updateMessages(messages = messages)
        if (messages.isNotEmpty()) {
            listChat.post { listChat.scrollToPosition(messages.size - 1) }
        }
    }
}
