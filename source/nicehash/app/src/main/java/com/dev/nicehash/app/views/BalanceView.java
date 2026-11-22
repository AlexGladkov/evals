package com.dev.nicehash.app.views;

import com.dev.nicehash.domain.models.Balance;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 15.07.18.
 * View for balance tab
 */
@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface BalanceView extends MvpView {
    void setupBalance(List<Balance> data);
    void startLoading();
    void endLoading();
    void showNoItems();
    void showError(String message);
    void setupBarItems(String unpaid, String usd);
}
