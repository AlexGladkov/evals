package com.dev.nicehash.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import android.app.Activity
import android.graphics.Color
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpAppCompatActivity


/**
 * Created by Alex Gladkov on 17.06.18.
 * Base activity for extension with theme support
 */
abstract class BaseActivity : MvpAppCompatActivity() {

    /** Global chat overlay (hosted on top of every screen's content) */
    private var chatOverlay: ChatOverlayView? = null
    private val chatDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(App.theme.styleRes)
        App.isDark = App.theme.isDark

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setStatusBarColor(App.theme.statusBarColorRes)
    }

    /**
     * Wrap the inflated layout so every screen automatically hosts the global
     * chat overlay (FAB + panel) next to its normal content. Screens themselves
     * stay untouched.
     */
    override fun setContentView(layoutResID: Int) {
        setContentView(view = layoutInflater.inflate(layoutResID, null))
    }

    override fun setContentView(view: View) {
        val wrapper = FrameLayout(this)
        wrapper.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT)
        wrapper.addView(view)

        chatOverlay = ChatOverlayView(this)
        wrapper.addView(chatOverlay)

        super.setContentView(wrapper)
        setupChatOverlay()
    }

    /** Fetch the app-scoped ChatRepository from DI and wire it to the overlay */
    private fun setupChatOverlay() {
        chatDisposables.clear()
        chatOverlay?.bind(repository = App.appComponent.chatRepository(), disposables = chatDisposables)
    }

    override fun onDestroy() {
        chatDisposables.clear()
        chatOverlay = null
        super.onDestroy()
    }

    private fun setStatusBarColor(color: Int) {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.BLACK
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}