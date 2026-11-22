package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;

import com.dev.nicehash.app.views.SettingsView;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.github.terrakok.cicerone.Router;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 24.06.18.
 * Presenter for settings activity
 */
@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private final Router router;
    private final ConfigurationRepository configurationRepository;

    public SettingsPresenter(Router router, ConfigurationRepository configurationRepository) {
        this.router = router;
        this.configurationRepository = configurationRepository;
    }

    public void performClick(int id) {
//        when (id) {
//            0 -> router.replaceScreen(ScreenKeys.Notifications.value)
//            1 -> router.replaceScreen(ScreenKeys.Themes.value)
//            2 -> router.replaceScreen(ScreenKeys.Tab.value)
//            3 -> router.replaceScreen(ScreenKeys.Language.value)
//            4 -> router.replaceScreen(ScreenKeys.Currencies.value)
//            5 -> router.replaceScreen(ScreenKeys.Service.value)
//            6 -> router.replaceScreen(ScreenKeys.Purchases.value)
//            7 -> router.replaceScreen(ScreenKeys.About.value)
//        }
    }

    @SuppressLint("CheckResult")
    public void fetchConfiguration() {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configuration -> {
                    getViewState().setupConfiguration(configuration);
                }, error -> {
                    error.printStackTrace();
                    getViewState().setupConfiguration(Configuration.defaultInstance());
                });
    }

    @SuppressLint("CheckResult")
    public void updateConfiguration(Configuration configuration) {
        configurationRepository.updateConfiguration(configuration)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                }, throwable -> {
                });
    }

    public void onBackClick() {
        router.exit();
    }
}
