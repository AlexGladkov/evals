package com.dev.nicehash.app.presenters;

import com.dev.nicehash.app.views.SplashView;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.models.Miner;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.domain.repositories.MinerRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Presenter for Splash Activity
 */
@InjectViewState
public class SplashPresenter extends MvpPresenter<SplashView> {
    private final String TAG = SplashPresenter.class.getSimpleName();

    private final ConfigurationRepository configurationRepository;
    private final MinerRepository minerRepository;

    public SplashPresenter(ConfigurationRepository configurationRepository,
                           MinerRepository minerRepository) {
        this.configurationRepository = configurationRepository;
        this.minerRepository = minerRepository;
    }

    public void fetchConfiguration() {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configuration -> {
                    minerRepository.loadMinersFromDb(configuration.getMinerId())
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(miners -> {
                                Miner selected = null;
                                for (Miner miner : miners) {
                                    if (miner.getId() == configuration.getMinerId()) {
                                        selected = miner;
                                        break;
                                    }
                                }

                                getViewState().setupConfiguration(configuration,
                                        selected != null ? selected : miners.get(0));
                            }, error -> error.printStackTrace());
                }, error -> {
                    error.printStackTrace();
                    getViewState().setupConfiguration(Configuration.defaultInstance(),
                            Miner.getDefaultInstance());
                });
    }
}
