package com.dev.nicehash.data.implementations;

import android.util.Log;

import com.dev.nicehash.data.providers.NicehashProvider;
import com.dev.nicehash.data.providers.models.ApiAlgo;
import com.dev.nicehash.domain.models.Income;
import com.dev.nicehash.domain.repositories.MoneyRepository;
import com.dev.nicehash.helpers.MathUtils;

import org.joda.time.DateTime;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Money repository implementation
 */
public class MoneyRepositoryImpl implements MoneyRepository {
    private final String TAG = MoneyRepositoryImpl.class.getSimpleName();

    private final NicehashProvider nicehashProvider;

    public MoneyRepositoryImpl(NicehashProvider nicehashProvider) {
        this.nicehashProvider = nicehashProvider;
    }

    /**
     * Miner unpaid balance
     * @param miner - wallet BTC address
     * @return single wrap of unpaid balance value
     */
    @Override
    public Single<Double> fetchUnpaidMoney(String miner) {
        return nicehashProvider.fetchStatsProviderEx(miner, 0L)
                .flatMap(result -> {
                    double sum = 0.0;
                    for (com.dev.nicehash.data.providers.models.ApiProviderCurrent current : result.getCurrent()) {
                        sum += current.getData().getUnpaid();
                    }
                    return Single.just(sum);
                });
    }

    /**
     * Miner income for week, day, month, year
     * @param miner - wallet BTC address
     * @return single wrap of Income model
     */
    @Override
    public Single<Income> fetchMinerIncome(String miner) {
        return nicehashProvider.fetchStatsProviderEx(miner, new DateTime().minusHours(25).getMillis() / 1000)
                .flatMap(result -> {
                    LinkedHashMap<Integer, ApiAlgo> profitabilityItems = new LinkedHashMap<>();
                    LinkedHashMap<Long, Double> chartItems = new LinkedHashMap<>();

                    for (com.dev.nicehash.data.providers.models.ApiProviderCurrent current : result.getCurrent()) {
                        profitabilityItems.put(current.getAlgo(),
                                new ApiAlgo((double) current.getData().getA(), current.getProfitability()));
                    }

                    for (com.dev.nicehash.data.providers.models.ApiProviderPast past : result.getPast()) {
                        for (com.dev.nicehash.data.providers.models.ApiProviderPastData data : past.getData()) {
                            long timestamp = data.getTime() * 300;
                            long localDateTime = new DateTime(timestamp * 1000).getMillis();

                            float speedObject = data.getA();
                            ApiAlgo pastAlgoItem = profitabilityItems.get(past.getAlgo());
                            double profitability = (pastAlgoItem != null ? pastAlgoItem.getPrice() : 0.0)
                                    * (pastAlgoItem != null ? pastAlgoItem.getSpeed() : 0.0);
                            double currentSpeed = pastAlgoItem != null ? pastAlgoItem.getSpeed() : 0.0;
                            double multi = speedObject / currentSpeed;
                            double pastProfitability = multi * profitability;

                            Double existing = chartItems.get(localDateTime);
                            if (existing != null) {
                                chartItems.put(localDateTime, existing + pastProfitability);
                            } else {
                                chartItems.put(localDateTime, pastProfitability);
                            }
                        }
                    }

                    long dayDuration = new DateTime().minusDays(1).getMillis();
                    Map<Long, Double> dayArray = new LinkedHashMap<>();
                    for (Map.Entry<Long, Double> entry : chartItems.entrySet()) {
                        if (entry.getKey() > dayDuration) {
                            dayArray.put(entry.getKey(), entry.getValue());
                        }
                    }

                    List<Double> dayValues = new LinkedList<>(dayArray.values());

                    // Kotlin named args: hourValue = 0.0, hourBtc = ..., dayValue = 0.0, dayBtc = ...,
                    // monthValue = 0.0, monthBtc = ..., weekValue = 0.0, weekBtc = ...
                    return Single.just(new Income(0.0,
                            MathUtils.getAverage(dayValues) / 24, 0.0,
                            MathUtils.getAverage(dayValues), 0.0,
                            MathUtils.getAverage(dayValues) * 31,
                            0.0, MathUtils.getAverage(dayValues) * 7));
                });
    }
}
