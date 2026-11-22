package com.dev.nicehash.app.presenters

import android.annotation.SuppressLint
import com.dev.nicehash.app.views.SettingsView
import com.dev.nicehash.domain.models.Configuration
import com.dev.nicehash.domain.repositories.ConfigurationRepository
import com.github.terrakok.cicerone.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

/**
 * Created by Alex Gladkov on 24.06.18.
 * Presenter for settings activity
 */
@InjectViewState
class SettingsPresenter(val router: Router, private val configurationRepository: ConfigurationRepository): MvpPresenter<SettingsView>() {

    @SuppressLint("CheckResult")
    fun fetchConfiguration() {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ configuration ->
                    viewState.setupConfiguration(configuration = configuration)
                }, { error ->
                    error.printStackTrace()
                    viewState.setupConfiguration(configuration = Configuration.defaultInstance())
                })
    }

    @SuppressLint("CheckResult")
    fun updateConfiguration(configuration: Configuration) {
        configurationRepository.updateConfiguration(configuration = configuration)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { })
    }

    fun onBackClick() {
        router.exit()
    }
}