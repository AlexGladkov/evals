package com.dev.nicehash.app.presenters;

import android.os.Bundle;

import com.dev.nicehash.R;
import com.dev.nicehash.app.views.MainView;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;
import com.github.mikephil.charting.data.Entry;
import com.github.terrakok.cicerone.Router;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 21.06.18.
 * Presenter for MainActivity
 */
@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private final String TAG = MainPresenter.class.getSimpleName();
    private float chartHeight = 0f;

    private final Router router;
    private final MinerRepository minerRepository;

    public MainPresenter(Router router, MinerRepository minerRepository) {
        this.router = router;
        this.minerRepository = minerRepository;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        minerRepository.fetchMinerStatistics();
    }

    public void onDeviceClick() {
//        router.replaceScreen(ScreenKeys.Choose.value)
    }

    public void onTabClick(int position) {
        getViewState().setupNavigation(position);
//        when (position) {
//            0 -> router.replaceScreen(ScreenKeys.Income.value)
//            1 -> router.replaceScreen(ScreenKeys.Balance.value)
//            2 -> router.replaceScreen(ScreenKeys.Payouts.value)
//            3 -> router.replaceScreen(ScreenKeys.Workers.value)
//        }
    }

    public void measureChartHeight(float screenHeight, float bottomBlockHeight,
                                   float topBlockHeight) {
        chartHeight = screenHeight - bottomBlockHeight - topBlockHeight;
        getViewState().setupChartHeight(chartHeight);
    }

    public void handleConfiguration(Bundle extras) {
        if (extras != null && extras.get(Keys.Configuration.getValue()) instanceof Configuration) {
            Configuration configuration = (Configuration) extras.get(Keys.Configuration.getValue());
            getViewState().setupConfiguration(configuration);
        }
    }

    public void handleScrolling(int scrollY, float daysBarHeight, float introBarHeight) {
        // MARK: - Setup intro bar position
        if (scrollY <= chartHeight + daysBarHeight) {
            getViewState().setupIntroBar(scrollY);
        } else {
            getViewState().setupIntroBar(chartHeight + daysBarHeight);
            // MARK: - Setup tab bar position
            if (scrollY > chartHeight + daysBarHeight + introBarHeight) {
                getViewState().setupTabBar(scrollY - chartHeight - daysBarHeight - introBarHeight);
            } else {
                getViewState().setupTabBar(0f);
            }
        }
    }

    public void onChartTimeClick(int position, int id, String miner) {
        getViewState().setupChartView(position);
        loadChartData(position, id, miner);
    }

    private void loadChartData(int position, int id, String miner) {
        ArrayList<Entry> emptyData = new ArrayList<>();
        long time = new DateTime().minusMinutes(70).getMillis() / 1000;
        int elapsedHours = 1;

        switch (position) {
            case 1: {
                elapsedHours = 6;
                time = new DateTime().minusHours(6).getMillis() / 1000;
                break;
            }
            case 2: {
                elapsedHours = 12;
                time = new DateTime().minusHours(12).getMillis() / 1000;
                break;
            }
            case 3: {
                elapsedHours = 24;
                time = new DateTime().minusDays(1).getMillis() / 1000;
                break;
            }
            case 4: {
                elapsedHours = 72;
                time = new DateTime().minusDays(3).getMillis() / 1000;
                break;
            }
            case 5: {
                elapsedHours = 24 * 7;
                time = new DateTime().minusDays(7).getMillis() / 1000;
                break;
            }
        }

        minerRepository.fetchMinerChart(miner, time, elapsedHours)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (!response.isEmpty()) {
                        for (int index = 0; index < response.size(); index++) {
                            emptyData.add(new Entry(index, (float) response.get(index).getY()));
                        }

                        getViewState().setupChartData(emptyData);
                    } else {
                        getViewState().showError(R.string.chart_no_data);
                    }
                }, error -> getViewState().showError(error.getLocalizedMessage()));
    }
}
