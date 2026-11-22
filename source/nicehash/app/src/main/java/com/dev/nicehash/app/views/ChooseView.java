package com.dev.nicehash.app.views;

import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.models.Miner;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 23.06.18.
 * View interface for ChooseActivity
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ChooseView extends MvpView {
    void setupMiners(List<Miner> list);
    void setupConfiguration(Configuration configuration);
}
