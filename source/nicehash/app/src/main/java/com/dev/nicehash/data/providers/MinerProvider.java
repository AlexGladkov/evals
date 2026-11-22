package com.dev.nicehash.data.providers;

import com.dev.nicehash.data.providers.models.ApiBalance;
import com.dev.nicehash.data.providers.models.ApiPayment;
import com.dev.nicehash.data.providers.models.ApiWorker;
import com.dev.nicehash.domain.models.ChartItem;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 05.08.18.
 * Provider for accessing base miner info
 */
public interface MinerProvider {
    void getStatistics(String miner, long time);

    /**
     * @return array of chart items to display
     * @param miner - wallet id
     * @param time - start position
     * @param elapsedHours - how many hours needs to display
     */
    Single<List<ChartItem>> getChart(String miner, long time, int elapsedHours);

    Single<List<ApiPayment>> getPayments(String miner);
    Single<List<ApiWorker>> getWorkers(String miner);
    Single<List<ApiBalance>> getBalance(String miner);
}
