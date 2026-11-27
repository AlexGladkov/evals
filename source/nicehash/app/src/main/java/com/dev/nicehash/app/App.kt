package com.dev.nicehash.app

import android.app.Application
import android.os.Trace
import com.dev.nicehash.app.di.*
import com.dev.nicehash.enums.Theme

/**
 * Created by agladkov on 17.06.18.
 * Class for application
 */
class App: Application() {

    companion object {
        var theme: Theme = Theme.DarkPurple
        var isDark: Boolean = false

        var isSettingsNeedsToReload = false
        var isChooseNeedsToReload = false
        var isMainNeedsToReload = false

        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Trace.beginSection("Dagger2")
        initializeDagger()
        Trace.endSection()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(app = this@App))
                .roomModule(RoomModule())
                .remoteModule(RemoteModule())
                .localNavigationModule(LocalNavigationModule())
                .build()
    }
}