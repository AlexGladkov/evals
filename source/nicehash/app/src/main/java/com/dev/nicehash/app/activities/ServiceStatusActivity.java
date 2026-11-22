package com.dev.nicehash.app.activities;

import android.os.Bundle;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.adapters.StatusAdapter;
import com.dev.nicehash.app.presenters.StatusPresenter;
import com.dev.nicehash.app.views.StatusView;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.domain.models.Status;
import com.dev.nicehash.helpers.ListConfig;

import java.util.List;

import moxy.presenter.InjectPresenter;

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for service status
 */
public class ServiceStatusActivity extends BaseActivity implements StatusView {
    private final StatusAdapter mAdapter = new StatusAdapter();

    @InjectPresenter
    public StatusPresenter statusPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_status);

//        imgServiceBack.setOnClickListener { onBackPressed() }

        ListConfig listConfig = new ListConfig.Builder(mAdapter)
                .setHasFixedSize(true)
                .setHasNestedScroll(false)
                .build(this);

//        listConfig.applyConfig(context = this@ServiceStatusActivity, recyclerView = recyclerServiceStatus)

        statusPresenter.fetchStatuses();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    // MARK: - View implementation
    @Override
    public void setupView(List<Status> data) {
        if (mAdapter.hasItems()) {
            mAdapter.updateItems(data);
        } else {
            mAdapter.setList(data);
        }
    }
}
