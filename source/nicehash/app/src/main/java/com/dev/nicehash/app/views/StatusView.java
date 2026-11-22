package com.dev.nicehash.app.views;

import com.dev.nicehash.domain.models.Status;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 25.07.18.
 * View for status activity
 * @see com.dev.nicehash.app.activities.ServiceStatusActivity
 */
@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface StatusView extends MvpView {
    void setupView(List<Status> data);
}
