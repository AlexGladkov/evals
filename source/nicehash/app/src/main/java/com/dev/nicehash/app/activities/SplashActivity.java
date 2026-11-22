package com.dev.nicehash.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.presenters.SplashPresenter;
import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.views.SplashView;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.models.Miner;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.domain.repositories.CurrencyRepository;
import com.dev.nicehash.domain.repositories.GeneralRepository;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Start activity (all logic threads starts from here)
 */
public class SplashActivity extends BaseActivity implements SplashView {
    private final String TAG = SplashActivity.class.getSimpleName();

    @Inject
    ConfigurationRepository configurationRepository;
    @Inject
    MinerRepository minerRepository;
    @Inject
    GeneralRepository generalRepository;
    @Inject
    CurrencyRepository currencyRepositorry;
    @Inject
    ExchangeServer exchangeServer;

    @InjectPresenter
    public SplashPresenter splashPresenter;

    @ProvidePresenter
    public SplashPresenter provideSplashPresenter() {
        return new SplashPresenter(configurationRepository, minerRepository);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        Log.e(TAG, "on create");
        setContentView(R.layout.activity_splash);

        splashPresenter.fetchConfiguration();
        currencyRepositorry.fetchExchangeRates();
    }

    // MARK: - View implementation
    @Override
    public void setupConfiguration(Configuration configuration, Miner miner) {
        Log.e(TAG, "miner name " + miner.getName() + ", id " + miner.getId());
        App.theme = configuration.getTheme();

        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra(Keys.Configuration.getValue(), configuration);
        mainIntent.putExtra(Keys.Miner.getValue(), miner.getHash());
        mainIntent.putExtra(Keys.MinerTitle.getValue(), miner.getName());
        startActivity(mainIntent);
    }
}
