package com.dev.nicehash.app.views;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 30.07.18.
 * View for Detail Activity
 */
@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface DetailView extends MvpView {
    void setupChartView(int position);
    void setupChartData(List<Entry> data);
}
