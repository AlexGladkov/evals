package com.dev.nicehash.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.adapters.CurrencyAdapter;
import com.dev.nicehash.app.presenters.CurrencyPresenter;
import com.dev.nicehash.app.views.CurrencyView;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.base.BaseAdapterCallback;
import com.dev.nicehash.domain.models.Currency;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.helpers.ListConfig;

import java.util.List;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for currencies settings
 */
public class CurrenciesActivity extends BaseActivity implements CurrencyView {
    private final String TAG = CurrenciesActivity.class.getSimpleName();
    private final CurrencyAdapter mAdapter = new CurrencyAdapter();

    @Inject
    ConfigurationRepository configurationRepository;

    @InjectPresenter
    public CurrencyPresenter currencyPresenter;

    @ProvidePresenter
    public CurrencyPresenter providePresenter() {
        return new CurrencyPresenter(configurationRepository);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);

//        imgCurrenciesBack.setOnClickListener { onBackPressed() }

        ListConfig listConfig = new ListConfig.Builder(mAdapter)
                .setHasNestedScroll(false)
                .setHasFixedSize(true)
                .build(getApplicationContext());

//        listConfig.applyConfig(context = applicationContext, recyclerView = recyclerCurrency)
        currencyPresenter.fetchCurrencies();

        mAdapter.attachCallback(new BaseAdapterCallback<Currency>() {
            @Override
            public void onItemClick(Currency model, View view) {
                if (!mAdapter.checkSameFirstPosition(model)) {
                    if (!mAdapter.checkIsPopular(model)) {
                        mAdapter.updateItem(0, new Currency(model.getId(),
                                model.getTitle(), model.getBtc(), R.string.currency_base,
                                model.getImgRes(), model.getPrototype()));
                    } else {
                        mAdapter.performPopularClicked(model);
                    }
                }

                currencyPresenter.setCurrency(model);
            }

            @Override
            public boolean onLongClick(Currency model, View view) {
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    // MARK: - View implementation
    @Override
    public void setupView(List<Currency> data) {
        if (mAdapter.hasItems()) {
            mAdapter.updateItems(data);
        } else {
            mAdapter.setList(data);
        }
    }
}
