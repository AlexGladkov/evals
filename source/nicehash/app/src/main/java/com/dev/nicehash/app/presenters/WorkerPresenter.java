package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;

import com.dev.nicehash.app.views.WorkerView;
import com.dev.nicehash.domain.models.WorkerHub;
import com.dev.nicehash.domain.repositories.MinerRepository;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 15.07.18.
 * View for worker tab
 */
@InjectViewState
public class WorkerPresenter extends MvpPresenter<WorkerView> {

    private final MinerRepository minerRepository;

    public WorkerPresenter(MinerRepository minerRepository) {
        this.minerRepository = minerRepository;
    }

    @SuppressLint("CheckResult")
    public void fetchWorkers(String miner, int id, boolean isRefresh) {
        if (id < 0) {
            ArrayList<WorkerHub> array = new ArrayList<>();
            ArrayList<WorkerHub.Worker> subArray = new ArrayList<>();
            // Worker ctor declared order: (title, difference, min, upDynamic, downDynamic)
            subArray.add(new WorkerHub.Worker("DaggerHashimoto", 0.25f, 68, 33.03f, 0f));
            subArray.add(new WorkerHub.Worker("Pascal", 0.2f, 46, 0.34f, 0.04f));
            array.add(new WorkerHub("Top", "EU", subArray));
            array.add(new WorkerHub("Bottom", "US", subArray));
            getViewState().setupWorkers(array);
        } else {
            if (isRefresh) getViewState().startLoading();
            minerRepository.fetchWorkers(miner)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        getViewState().endLoading();
                        if (response.isEmpty()) {
                            getViewState().setupNoItems();
                        } else {
                            getViewState().setupWorkers(response);
                        }
                    }, error -> {
                        getViewState().endLoading();
                        getViewState().showError(error.getLocalizedMessage());
                    });
        }
    }
}
