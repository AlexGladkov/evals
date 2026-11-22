package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dev.nicehash.R;
import com.dev.nicehash.app.views.CurrencyView;
import com.dev.nicehash.domain.models.Currency;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.helpers.EnumCollections;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 25.07.18.
 * Presenter for currency activity (domain level)
 * @see com.dev.nicehash.app.activities.CurrenciesActivity
 */
@InjectViewState
public class CurrencyPresenter extends MvpPresenter<CurrencyView> {
    private final String TAG = CurrencyPresenter.class.getSimpleName();

    private final ConfigurationRepository configurationRepository;

    public CurrencyPresenter(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public void fetchCurrencies() {
        LinkedList<Currency> data = new LinkedList<>();

        data.add(new Currency(EnumCollections.Currency.Russian.getId(),
                EnumCollections.Currency.Russian.getTitleRes(), "413.558,23", R.string.currency_base,
                EnumCollections.Currency.Russian.getFlagRes(), EnumCollections.Currency.Russian));

        data.add(new Currency(EnumCollections.Currency.Euro.getId(), EnumCollections.Currency.Euro.getTitleRes(),
                "5,664", R.string.currency_popular, EnumCollections.Currency.Euro.getFlagRes(),
                EnumCollections.Currency.Euro));
        data.add(new Currency(EnumCollections.Currency.BritainPound.getId(),
                EnumCollections.Currency.BritainPound.getTitleRes(), "5,104", -1,
                EnumCollections.Currency.BritainPound.getFlagRes(),
                EnumCollections.Currency.BritainPound));
        data.add(new Currency(EnumCollections.Currency.USD.getId(),
                EnumCollections.Currency.USD.getTitleRes(), "7,188", -1,
                EnumCollections.Currency.USD.getFlagRes(),
                EnumCollections.Currency.USD));

        data.add(new Currency(EnumCollections.Currency.Australian.getId(), EnumCollections.Currency.Australian.getTitleRes(),
                "9,344", R.string.currency_all, EnumCollections.Currency.Australian.getFlagRes(),
                EnumCollections.Currency.Australian));
        data.add(new Currency(EnumCollections.Currency.Belgium.getId(), EnumCollections.Currency.Belgium.getTitleRes(),
                "11,404", -1, EnumCollections.Currency.Belgium.getFlagRes(),
                EnumCollections.Currency.Belgium));
        data.add(new Currency(EnumCollections.Currency.Canada.getId(), EnumCollections.Currency.Canada.getTitleRes(),
                "9,273", -1, EnumCollections.Currency.Canada.getFlagRes(),
                EnumCollections.Currency.Canada));
        data.add(new Currency(EnumCollections.Currency.CHF.getId(), EnumCollections.Currency.CHF.getTitleRes(),
                "6,874", -1, EnumCollections.Currency.CHF.getFlagRes(),
                EnumCollections.Currency.CHF));
        data.add(new Currency(EnumCollections.Currency.China.getId(), EnumCollections.Currency.China.getTitleRes(),
                "45,273", -1, EnumCollections.Currency.China.getFlagRes(),
                EnumCollections.Currency.China));
        data.add(new Currency(EnumCollections.Currency.Czech.getId(), EnumCollections.Currency.Czech.getTitleRes(),
                "148,404", -1, EnumCollections.Currency.Czech.getFlagRes(),
                EnumCollections.Currency.Czech));
        data.add(new Currency(EnumCollections.Currency.Dania.getId(), EnumCollections.Currency.Dania.getTitleRes(),
                "43,480.21", -1, EnumCollections.Currency.Dania.getFlagRes(),
                EnumCollections.Currency.Dania));

        getViewState().setupView(data);
    }

    @SuppressLint("CheckResult")
    public void setCurrency(Currency currency) {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(config -> {
                    config.setCurrency(currency.getPrototype());
                    configurationRepository.updateConfiguration(config)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {
                                Log.e(TAG, "configuration updated");
                            }, error -> {
                                Log.e(TAG, "error updating " + error.getLocalizedMessage());
                            });
                });
    }
}
