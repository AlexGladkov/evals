package com.dev.nicehash.data.implementations;

import android.util.Log;

import com.dev.nicehash.data.converters.BalanceConverter;
import com.dev.nicehash.data.converters.MinerConverter;
import com.dev.nicehash.data.converters.PaymentsConverter;
import com.dev.nicehash.data.converters.WorkerConverter;
import com.dev.nicehash.data.providers.MinerProvider;
import com.dev.nicehash.data.room.RoomAppDataSource;
import com.dev.nicehash.data.room.models.MinerEntity;
import com.dev.nicehash.domain.models.Balance;
import com.dev.nicehash.domain.models.ChartItem;
import com.dev.nicehash.domain.models.Miner;
import com.dev.nicehash.domain.models.Payout;
import com.dev.nicehash.domain.models.WorkerHub;
import com.dev.nicehash.domain.repositories.MinerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Repository for any miners activity
 */
public class MinerRepositoryImpl implements MinerRepository {
    private final String TAG = MinerRepositoryImpl.class.getSimpleName();

    private final RoomAppDataSource appDataSource;
    private final MinerProvider minerProvider;
    private final MinerConverter minerConverter;
    private final PaymentsConverter paymentsConverter;
    private final BalanceConverter balanceConverter;
    private final WorkerConverter workerConverter;

    public MinerRepositoryImpl(RoomAppDataSource appDataSource, MinerProvider minerProvider,
                               MinerConverter minerConverter, PaymentsConverter paymentsConverter,
                               BalanceConverter balanceConverter, WorkerConverter workerConverter) {
        this.appDataSource = appDataSource;
        this.minerProvider = minerProvider;
        this.minerConverter = minerConverter;
        this.paymentsConverter = paymentsConverter;
        this.balanceConverter = balanceConverter;
        this.workerConverter = workerConverter;
    }

    @Override
    public void fetchMinerStatistics() {
        minerProvider.getBalance("3Aqj4o27ixprba67GvBctDkXijxxpbD4hS")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    //                    Log.e(TAG, "response $response")
                }, error -> {
                    //                    Log.e(TAG, "error $error")
                });
    }

    @Override
    public Single<List<Balance>> fetchMinerBalance(String miner) {
        return minerProvider.getBalance(miner)
                .flatMap(response -> {
                    List<Balance> converted = new ArrayList<>();
                    for (com.dev.nicehash.data.providers.models.ApiBalance model : response) {
                        converted.add(balanceConverter.apiToModel(model));
                    }
                    return Single.just(converted);
                });
    }

    @Override
    public Single<List<WorkerHub>> fetchWorkers(String miner) {
        return minerProvider.getWorkers(miner)
                .flatMap(response -> {
                    List<WorkerHub.Worker> workers = new ArrayList<>();
                    for (com.dev.nicehash.data.providers.models.ApiWorker model : response) {
                        workers.add(workerConverter.apiToModel(model));
                    }
                    return Single.just(workerConverter.workerToHub(workers));
                });
    }

    @Override
    public Single<List<Payout>> fetchMinerPayments(String miner) {
        return minerProvider.getPayments(miner)
                .flatMap(response -> {
                    List<Payout> converted = new ArrayList<>();
                    for (com.dev.nicehash.data.providers.models.ApiPayment model : response) {
                        converted.add(paymentsConverter.apiToModel(model));
                    }
                    return Single.just(converted);
                });
    }

    @Override
    public Single<List<ChartItem>> fetchMinerChart(String miner, long time, int elapsedHours) {
        return minerProvider.getChart(miner, time, elapsedHours);
    }

    @Override
    public Single<List<Miner>> loadMinersFromDb(int minerId) {
        return Single.create(subscriber -> {
            try {
                List<MinerEntity> miners = appDataSource.getMinerDao().getMiners();

                List<Miner> result = new ArrayList<>();
                if (miners.isEmpty()) {
                    result.add(new Miner(-1, "Demo Miner", "Your BTC wallet address", false));
                } else {
                    List<Miner> mapped = new ArrayList<>();
                    for (MinerEntity entity : miners) {
                        mapped.add(minerConverter.dbToModel(entity, minerId));
                    }
                    Collections.sort(mapped, (a, b) -> Boolean.compare(a.isSelected(), b.isSelected()));
                    Collections.reverse(mapped);
                    result.addAll(mapped);
                }

                subscriber.onSuccess(result);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Single<Boolean> updateMiner(Miner miner, String newName, String newHash) {
        return Single.create(subscriber -> {
            // Kotlin port: onError without return — execution continues (bug preserved)
            if (miner == null) subscriber.onError(new Throwable(new NullPointerException()));
            try {
                appDataSource.getMinerDao().addMiner(new MinerEntity(miner != null ? miner.getId() : null,
                        newName, newHash, ""));
                subscriber.onSuccess(true);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Single<Long> addMiner(Miner miner) {
        return Single.create(subscriber -> {
            try {
                subscriber.onSuccess(appDataSource.getMinerDao().addMiner(
                        new MinerEntity(null, miner.getName(), miner.getHash(), "")));
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
