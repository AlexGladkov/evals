package com.dev.nicehash.app.activities;

import android.os.Bundle;
import android.util.Log;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for notifications settings
 */
public class NotificationsActivity extends BaseActivity {
    private final String TAG = NotificationsActivity.class.getSimpleName();

    // MARK: - Presenter setup
    @Inject
    ConfigurationRepository configurationRepository;

    // MARK: - UI Setup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

//        imgNotificationsBack.setOnClickListener { onBackPressed() }

        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configuration -> {
                    setupConfiguration(configuration);
                }, error -> {
                    error.printStackTrace();
                    setupConfiguration(Configuration.defaultInstance());
                });
    }

    private void onChangeClick(int position, boolean isChecked) {
        switch (position) {
            case 0:
                configuration.setPayments(isChecked);
                break;
            case 1:
                configuration.setMaintenance(isChecked);
                break;
            case 2:
                configuration.setTop(isChecked);
                break;
            case 3:
                configuration.setBottom(isChecked);
                break;
            case 4:
                configuration.setStrix(isChecked);
                break;
            case 5:
                configuration.setSound(isChecked);
                break;
            case 6:
                configuration.setVibro(isChecked);
                break;
        }

        Log.e(TAG, "configuration bottom " + configuration.isBottom());

        configurationRepository.updateConfiguration(configuration)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                }, error -> {
                    Log.e(TAG, "update error " + error.getLocalizedMessage());
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    private Configuration configuration;

    private void setupConfiguration(Configuration configuration) {
        this.configuration = configuration;
        Log.e(TAG, "configuration bottom " + configuration.isBottom());
//        swNotificationsPayments.isChecked = configuration.isPayments
//        swNotificationsMaintenance.isChecked = configuration.isMaintenance
//
//        swNotificationsTop.isChecked = configuration.isTop
//        swNotificationsBottom.isChecked = configuration.isBottom
//        swNotificationsStrix.isChecked = configuration.isStrix
//
//        swNotificationsSound.isChecked = configuration.isSound
//        swNotificationsVibro.isChecked = configuration.isVibro
//
//        swNotificationsPayments.setOnCheckedChangeListener { _, isChecked ->
//            onChangeClick(position = 0, isChecked = isChecked)
//        }
//        swNotificationsMaintenance.setOnCheckedChangeListener { _, isChecked ->
//            onChangeClick(position = 1, isChecked = isChecked)
//        }
//        swNotificationsTop.setOnCheckedChangeListener { _, isChecked ->
//            onChangeClick(position = 2, isChecked = isChecked)
//        }
//        swNotificationsBottom.setOnCheckedChangeListener { _, isChecked ->
//            onChangeClick(position = 3, isChecked = isChecked)
//        }
//        swNotificationsStrix.setOnCheckedChangeListener { _, isChecked ->
//            onChangeClick(position = 4, isChecked = isChecked)
//        }
//        swNotificationsSound.setOnCheckedChangeListener { _, isChecked ->
//            onChangeClick(position = 5, isChecked = isChecked)
//        }
//        swNotificationsVibro.setOnCheckedChangeListener { _, isChecked ->
//            onChangeClick(position = 6, isChecked = isChecked)
//        }
    }
}
