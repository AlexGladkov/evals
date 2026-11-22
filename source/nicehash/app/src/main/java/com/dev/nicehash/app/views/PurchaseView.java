package com.dev.nicehash.app.views;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface PurchaseView extends MvpView {
    void showError(int message);
    void makeTestBuy();
}
