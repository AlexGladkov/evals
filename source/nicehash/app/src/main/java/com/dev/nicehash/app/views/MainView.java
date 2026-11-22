package com.dev.nicehash.app.views;

import com.dev.nicehash.domain.models.Configuration;
import com.github.mikephil.charting.data.Entry;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 21.06.18.
 * View interface for MainActivity
 */
@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface MainView extends MvpView {
    void setupNavigation(int position);
    void setupConfiguration(Configuration configuration);
    void setupChartView(int position);
    void setupChartData(List<Entry> data);
    void setupChartHeight(float value);
    void setupIntroBar(float introBarPosition);
    void setupTabBar(float tabBarPosition);
    void displayNavigationInfo(String leftValue, String rightValue);
    void showError(int message);
    void showError(String message);
}
