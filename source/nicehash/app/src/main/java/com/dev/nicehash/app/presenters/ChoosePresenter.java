package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.dev.nicehash.app.views.ChooseView;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.models.Miner;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;
import com.github.terrakok.cicerone.Router;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Presenter for ChooseActivity
 */
@InjectViewState
public class ChoosePresenter extends MvpPresenter<ChooseView> {
    private final String TAG = ChoosePresenter.class.getSimpleName();

    private final Router router;
    private final MinerRepository minerRepository;
    private final ConfigurationRepository configurationRepository;

    public ChoosePresenter(Router router, MinerRepository minerRepository,
                           ConfigurationRepository configurationRepository) {
        this.router = router;
        this.minerRepository = minerRepository;
        this.configurationRepository = configurationRepository;
    }

    @SuppressLint("CheckResult")
    public void loadMiners() {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configuration -> {
                    minerRepository.loadMinersFromDb(configuration.getMinerId())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                getViewState().setupMiners(result);
                            });
                }, error -> {

                });
    }

    @SuppressLint("CheckResult")
    public void onMinerClick(Miner miner) {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configuration -> {
                    configuration.setMinerId(miner.getId());
                    configurationRepository.updateConfiguration(configuration)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {
//                                router.replaceScreen(ScreenKeys.Splash.value)
                            }, error -> {

                            });
                }, error -> {

                });
    }

    public void onGearClick() {
//        router.replaceScreen(ScreenKeys.Settings.value)
    }

    public void onAddClick() {
//        router.replaceScreen(ScreenKeys.Add.value)
    }

    public void editClick(Miner miner) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Keys.Miner.getValue(), miner);
//        router.replaceScreen(ScreenKeys.Edit.value, bundle)
    }

    public void deleteClick(Miner miner) {

    }

    public void handleConfiguration(Bundle extras) {
        if (extras != null && extras.get(Keys.Configuration.getValue()) instanceof Configuration) {
            Configuration configuration = (Configuration) extras.get(Keys.Configuration.getValue());
            getViewState().setupConfiguration(configuration);
        }
    }
}
