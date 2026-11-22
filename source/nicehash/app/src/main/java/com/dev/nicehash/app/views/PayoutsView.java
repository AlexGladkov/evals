package com.dev.nicehash.app.views;

import com.dev.nicehash.domain.models.Payout;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 15.07.18.
 * View for payouts tab
 */
@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface PayoutsView extends MvpView {
    void setupPayouts(List<Payout> data);
    void setupNoItems();
    void showError(String message);
    void startLoading();
    void endLoading();
    void setupBarInfo(double paidBTC, float usd);
}
