package com.dev.nicehash.app.views;

import com.dev.nicehash.domain.models.Currency;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 25.07.18.
 * View for currency activity
 * @see com.dev.nicehash.app.activities.CurrenciesActivity
 */
@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface CurrencyView extends MvpView {
    void setupView(List<Currency> data);
}
