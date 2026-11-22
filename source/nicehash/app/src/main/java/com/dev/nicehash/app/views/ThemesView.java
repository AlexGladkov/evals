package com.dev.nicehash.app.views;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 21.07.18.
 * View for Themes Activity
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ThemesView extends MvpView {
    void setupTab(int position);
    void switchTheme(boolean isLight, int color);
    void setupView(boolean isLight, int color);
}
