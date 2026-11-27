package com.dev.nicehash.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.presenters.ThemesPresenter
import com.dev.nicehash.app.views.ThemesView
import com.dev.nicehash.domain.repositories.ConfigurationRepository
import com.dev.nicehash.enums.Keys
import com.dev.nicehash.enums.styleRes
import com.github.terrakok.cicerone.NavigatorHolder
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for theme settings
 */
class ThemesActivity: MvpAppCompatActivity(), ThemesView {
    private val TAG = ThemesActivity::class.java.simpleName

    @Inject lateinit var configurationRepository: ConfigurationRepository
    @Inject lateinit var navigatorHolder: NavigatorHolder

    @InjectPresenter
    lateinit var themesPresenter: ThemesPresenter
    @ProvidePresenter
    fun provideThemesPresenter(): ThemesPresenter {
        return ThemesPresenter(configurationRepository = configurationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(activity = this@ThemesActivity)
        if (intent?.extras == null) {
            setTheme(App.theme.styleRes)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (intent?.extras == null) {
            themesPresenter.setupAppTheme()
        } else {
            themesPresenter.setColor(intent?.extras?.get(Keys.Color.value) as? Int ?: 0)
            themesPresenter.setLight(intent?.extras?.get(Keys.Light.value) as? Boolean ?: true)
            themesPresenter.setupView()
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    // MARK: - View implementation
    override fun setupTab(position: Int) {
        themesPresenter.setupTheme(isLight = position == 0)
    }

    override fun setupView(isLight: Boolean, color: Int) {

    }

    override fun switchTheme(isLight: Boolean, color: Int) {
        val intent = Intent(applicationContext, ThemesActivity::class.java)
        intent.putExtra(Keys.Light.value, isLight)
        intent.putExtra(Keys.Color.value, color)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}