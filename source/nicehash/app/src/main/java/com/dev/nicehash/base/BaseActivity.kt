package com.dev.nicehash.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import android.app.Activity
import android.graphics.Color
import moxy.MvpAppCompatActivity


/**
 * Created by Alex Gladkov on 17.06.18.
 * Base activity for extension with theme support
 */
abstract class BaseActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(App.theme.styleRes)
        App.isDark = App.theme.isDark

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setStatusBarColor(App.theme.statusBarColorRes)
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