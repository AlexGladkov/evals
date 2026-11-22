package com.dev.nicehash.app.views;

import com.dev.nicehash.domain.models.WorkerHub;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

/**
 * Created by Alex Gladkov on 15.07.18.
 * View for worker tab
 */
@StateStrategyType(value = OneExecutionStateStrategy.class)
public interface WorkerView extends MvpView {
    void startLoading();
    void endLoading();
    void setupWorkers(List<WorkerHub> data);
    void setupNoItems();
    void showError(String message);
}
