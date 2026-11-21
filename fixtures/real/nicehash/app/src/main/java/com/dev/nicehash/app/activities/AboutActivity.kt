package com.dev.nicehash.app.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.compose.AboutScreen
import com.dev.nicehash.base.BaseActivity
import timber.log.Timber

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for about information.
 * Migrated to Jetpack Compose.
 */
class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pInfo = try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "Failed to get package info")
            null
        }

        val versionName = pInfo?.versionName ?: "—"
        val versionCode = pInfo?.versionCode?.toString() ?: "—"

        // Use ComposeView with late content setting to avoid ViewTreeLifecycleOwner crash.
        // setContent is deferred to after setContentView so the view is attached to the
        // window (which already has ViewTree owners set by ComponentActivity).
        val composeView = ComposeView(this)
        setContentView(composeView)

        composeView.setContent {
            AboutScreen(
                appVersion = versionName,
                appBuild = versionCode,
                isDark = App.isDark,
                onBackClick = { onBackPressed() },
                onWebsiteClick = {
                    openUrl("https://getsignal.co")
                },
                onTwitterClick = {
                    openUrl("https://twitter.com/getsignal")
                },
                onAuthorClick = {
                    openUrl("https://getsignal.co")
                },
                onDeveloperClick = {
                    openUrl("https://getsignal.co")
                },
                onSaverClick = {
                    openUrl("https://getsignal.co")
                }
            )
        }
    }

    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            Timber.e(e, "Failed to open URL: %s", url)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
    }
}
