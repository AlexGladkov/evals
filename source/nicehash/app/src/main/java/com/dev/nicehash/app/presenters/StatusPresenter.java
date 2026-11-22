package com.dev.nicehash.app.presenters;

import com.dev.nicehash.app.views.StatusView;
import com.dev.nicehash.domain.models.Status;
import com.dev.nicehash.helpers.EnumCollections;

import java.util.ArrayList;
import java.util.List;

import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 25.07.18.
 * Presenter for service status activity
 * @see com.dev.nicehash.app.activities.ServiceStatusActivity
 */
@InjectViewState
public class StatusPresenter extends MvpPresenter<StatusView> {

    public void fetchStatuses() {
        List<Status> data = new ArrayList<>();
        data.add(new Status(0, " ", "Miner statistics", EnumCollections.Status.Green));
        data.add(new Status(1, "", "Nicehash miner endpoint", EnumCollections.Status.Green));
        data.add(new Status(2, "", "api.nicehash.com", EnumCollections.Status.Green));
        data.add(new Status(3, "", "www.nicehash.com", EnumCollections.Status.Yellow));

        data.add(new Status(0, "Servers", "Brazil", EnumCollections.Status.Green));
        data.add(new Status(1, "", "China", EnumCollections.Status.Green));
        data.add(new Status(2, "", "Japan", EnumCollections.Status.Red));
        data.add(new Status(3, "", "Europe", EnumCollections.Status.Green));
        getViewState().setupView(data);
    }
}
