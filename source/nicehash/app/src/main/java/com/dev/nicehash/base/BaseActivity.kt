package com.dev.nicehash.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.enums.Theme
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
        when (App.theme) {
            Theme.LightBlue -> setTheme(R.style.LightBlueTheme)
            Theme.LightGreen -> setTheme(R.style.LightGreenTheme)
            Theme.LightOrange -> setTheme(R.style.LightOrangeTheme)
            Theme.LightRed -> setTheme(R.style.LightRedTheme)
            Theme.LightPurple -> setTheme(R.style.LightPurpleTheme)
            Theme.DarkBlue -> setTheme(R.style.DarkBlueTheme)
            Theme.DarkGreen -> setTheme(R.style.DarkGreenTheme)
            Theme.DarkOrange -> setTheme(R.style.DarkOrangeTheme)
            Theme.DarkRed -> setTheme(R.style.DarkRedTheme)
            Theme.DarkPurple -> setTheme(R.style.DarkPurpleTheme)
        }

        App.isDark = App.theme == Theme.DarkBlue || App.theme == Theme.DarkPurple ||
                App.theme == Theme.DarkRed || App.theme == Theme.DarkOrange || App.theme == Theme.DarkGreen

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setStatusBarColor(when {
            App.theme == Theme.DarkPurple -> R.color.dark_status_bar_color
            App.theme == Theme.DarkBlue -> R.color.dark_status_bar_color
            App.theme == Theme.DarkRed -> R.color.dark_status_bar_color
            App.theme == Theme.DarkOrange -> R.color.dark_status_bar_color
            App.theme == Theme.DarkGreen -> R.color.dark_status_bar_color
            else -> R.color.light_status_bar_color
        })
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