package com.dev.nicehash.app.activities

import android.content.Intent
import android.os.Bundle
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.interfaces.RouterProvider
import com.dev.nicehash.app.presenters.SettingsPresenter
import com.dev.nicehash.app.views.SettingsView
import com.dev.nicehash.base.BaseActivity
import com.dev.nicehash.domain.models.Configuration
import com.dev.nicehash.domain.repositories.ConfigurationRepository
import com.dev.nicehash.enums.ScreenKeys
import com.github.terrakok.cicerone.Back
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.Router
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

/**
 * Created by Alex Gladkov on 23.06.18.
 * Activity for user settings
 */
class SettingsActivity : BaseActivity(), RouterProvider, SettingsView {
    private val TAG = SettingsActivity::class.java.simpleName

    // MARK: - Injects
    @Inject lateinit var routerSettings: Router
    @Inject lateinit var navigatorHolder: NavigatorHolder
    @Inject lateinit var configurationRepository: ConfigurationRepository

    // MARK: - Presenter
    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    @ProvidePresenter
    fun provideSettingsPresenter(): SettingsPresenter {
        return SettingsPresenter(router = routerSettings, configurationRepository = configurationRepository)
    }

    // MARK: - Navigation
    private val navigator = object : Navigator {
        override fun applyCommands(commands: Array<out Command>) {
            commands.forEach {
                applyCommand(it)
            }
        }
    }

    private fun applyCommand(command: Command) {
        when (command) {
            is Back -> finish()
            is Replace -> {
                when (command.screen.screenKey) {
                    ScreenKeys.Notifications.value -> {
                        startActivity(Intent(applicationContext, NotificationsActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    ScreenKeys.Themes.value -> {
                        startActivity(Intent(applicationContext, ThemesActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    ScreenKeys.Currencies.value -> {
                        startActivity(Intent(applicationContext, CurrenciesActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    ScreenKeys.Service.value -> {
                        startActivity(Intent(applicationContext, ServiceStatusActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    ScreenKeys.Purchases.value -> {
                        startActivity(Intent(applicationContext, PurchasesActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    ScreenKeys.About.value -> {
                        startActivity(Intent(applicationContext, AboutActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(activity = this@SettingsActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onResume() {
        super.onResume()
        settingsPresenter.fetchConfiguration()

        if (App.isSettingsNeedsToReload) {
            App.isSettingsNeedsToReload = false
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    // MARK: - RouterProvider implementation
    override fun getRouter(): Router {
        return routerSettings
    }

    // MARK: - View implementation
    private var currentConfiguration = Configuration.defaultInstance()
    override fun setupConfiguration(configuration: Configuration) {
        this.currentConfiguration = configuration
    }
}