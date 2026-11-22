package com.dev.nicehash.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.adapters.WorkerHubAdapter;
import com.dev.nicehash.app.presenters.WorkerPresenter;
import com.dev.nicehash.app.views.MainView;
import com.dev.nicehash.app.views.WorkerView;
import com.dev.nicehash.base.BaseContainer;
import com.dev.nicehash.domain.models.WorkerHub;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;
import com.dev.nicehash.helpers.ListConfig;
import com.github.terrakok.cicerone.Navigator;

import java.util.List;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for workers tab
 */
public class WorkersFragment extends BaseContainer implements WorkerView {

    // MARK: - Injects
    @Inject
    MinerRepository minerRepository;

    // MARK: - Presenter setup
    @InjectPresenter
    public WorkerPresenter workersPresenter;

    @ProvidePresenter
    public WorkerPresenter provideWorkerPresenter() {
        return new WorkerPresenter(minerRepository);
    }

    // MARK: - UI Setup
    private final WorkerHubAdapter mAdapter = new WorkerHubAdapter();

    public static WorkersFragment getNewInstance(String name, String miner, int minerID) {
        WorkersFragment fragment = new WorkersFragment();
        Bundle args = new Bundle();
        args.putString(Keys.Name.getValue(), name);
        args.putString(Keys.Miner.getValue(), miner);
        args.putInt(Keys.MinerID.getValue(), minerID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    private Navigator navigator = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workers, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        if (context != null) {
            ListConfig listConfig = new ListConfig.Builder(mAdapter)
                    .setHasFixedSize(true)
                    .setHasNestedScroll(true)
                    .build(context);
//            listConfig.applyConfig(it, recyclerWorkers)
        }

        Bundle arguments = getArguments();
        workersPresenter.fetchWorkers(arguments != null && arguments.getString(Keys.Miner.getValue()) != null
                        ? arguments.getString(Keys.Miner.getValue()) : "",
                arguments != null ? arguments.getInt(Keys.MinerID.getValue(), -1) : -1,
                mAdapter.getItems().isEmpty());

        if (getActivity() instanceof MainView) {
            ((MainView) getActivity()).displayNavigationInfo("", "");
        }
    }

    @Override
    public Navigator getNavigator() {
        return null;
    }

    // MARK: - Base implementation
    @Override
    public void doubleTap() {

    }

    @Override
    public void updateContainer() {

    }

    // MARK: - View implementation
    @Override
    public void setupWorkers(List<WorkerHub> data) {
        if (mAdapter.hasItems()) {
            mAdapter.updateItems(data);
        } else {
            mAdapter.setList(data);
        }

//        recyclerWorkers.visibility = View.VISIBLE
//        txtWorkersNoItems.visibility = View.GONE
    }

    @Override
    public void setupNoItems() {
//        recyclerWorkers.visibility = View.GONE
//        txtWorkersNoItems.visibility = View.VISIBLE
    }

    @Override
    public void startLoading() {
//        cpvWorkers.visibility = View.VISIBLE
//        txtWorkersNoItems.visibility = View.GONE
//        recyclerWorkers.visibility = View.GONE
    }

    @Override
    public void endLoading() {
//        cpvWorkers.visibility = View.GONE
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
