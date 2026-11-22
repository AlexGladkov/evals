package com.dev.nicehash.domain.repositories;

import com.dev.nicehash.data.providers.models.ApiPayment;
import com.dev.nicehash.domain.models.Balance;
import com.dev.nicehash.domain.models.ChartItem;
import com.dev.nicehash.domain.models.Miner;
import com.dev.nicehash.domain.models.Payout;
import com.dev.nicehash.domain.models.WorkerHub;

import androidx.annotation.Nullable;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Repository for Miners API and DB
 */
public interface MinerRepository {
    Single<List<Miner>> loadMinersFromDb(int minerId);
    Single<Long> addMiner(Miner miner);
    Single<Boolean> updateMiner(@Nullable Miner miner, String newName, String newHash);
    void fetchMinerStatistics();
    Single<List<Payout>> fetchMinerPayments(String miner);
    Single<List<Balance>> fetchMinerBalance(String miner);
    Single<List<WorkerHub>> fetchWorkers(String miner);
    Single<List<ChartItem>> fetchMinerChart(String miner, long time, int elapsedHours);
}
