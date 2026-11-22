package com.dev.nicehash.data.providers.implementations;

import android.util.Log;

import com.dev.nicehash.data.providers.MinerProvider;
import com.dev.nicehash.data.providers.models.ApiAlgo;
import com.dev.nicehash.data.providers.models.ApiBalance;
import com.dev.nicehash.data.providers.models.ApiPayment;
import com.dev.nicehash.data.providers.models.ApiWorker;
import com.dev.nicehash.data.providers.models.ApiResponse;
import com.dev.nicehash.data.providers.services.RemoteMinerService;
import com.dev.nicehash.domain.models.ChartItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 05.08.18.
 * Provider implementation for Miner provider interface
 */
public class MinerProviderImpl implements MinerProvider {
    private final String TAG = MinerProviderImpl.class.getSimpleName();
    private final int TIME_CAPACITY = 6;

    private final RemoteMinerService remoteMinerService;
    private final Gson gson;

    public MinerProviderImpl(RemoteMinerService remoteMinerService, Gson gson) {
        this.remoteMinerService = remoteMinerService;
        this.gson = gson;
    }

    @Override
    public Single<List<ChartItem>> getChart(String miner, long time, int elapsedHours) {
        return Single.create(subscriber -> {
            remoteMinerService.getWorkerHistory("stats.provider.ex", miner, time)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(chartResponse -> {
                        JsonElement pastEl = chartResponse.getResult().get("past");
                        JsonArray past = pastEl != null ? pastEl.getAsJsonArray() : null;
                        JsonElement currentEl = chartResponse.getResult().get("current");
                        JsonArray current = currentEl != null ? currentEl.getAsJsonArray() : null;
                        LinkedHashMap<Integer, ApiAlgo> profitabilityItems = new LinkedHashMap<>();
                        LinkedHashMap<Long, Double> chartItems = new LinkedHashMap<>();

                        if (current != null) {
                            for (JsonElement algo : current) {
                                JsonElement accepted = algo.getAsJsonObject().get("data").getAsJsonArray().get(0)
                                        .getAsJsonObject().get("a");
                                if (accepted != null) {
                                    JsonElement acceptedElement = accepted;
                                    JsonElement algoEl = algo.getAsJsonObject().get("algo");
                                    profitabilityItems.put(algoEl != null ? algoEl.getAsInt() : -1,
                                            new ApiAlgo(acceptedElement.getAsDouble(),
                                                    algo.getAsJsonObject().get("profitability") != null
                                                            ? algo.getAsJsonObject().get("profitability").getAsDouble()
                                                            : 0.0));
                                }
                            }
                        }

                        if (past != null) {
                            for (JsonElement item : past) {
                                JsonArray data = item.getAsJsonObject().get("data").getAsJsonArray();
                                JsonElement algoEl = item.getAsJsonObject().get("algo");
                                int algo = algoEl != null ? algoEl.getAsInt() : -2;
                                for (JsonElement dataElem : data) {
                                    long timestamp = dataElem.getAsJsonArray().get(0).getAsLong() * 300;
                                    long localDateTime = new DateTime(timestamp * 1000).getMillis();

                                    JsonObject speedObject = dataElem.getAsJsonArray().get(1).getAsJsonObject();
                                    JsonElement aEl = speedObject.get("a");
                                    if (aEl != null) {
                                        double value = aEl.getAsDouble();
                                        ApiAlgo algoItem = profitabilityItems.get(algo);
                                        double profitability = (algoItem != null ? algoItem.getPrice() : 0.0)
                                                * (algoItem != null ? algoItem.getSpeed() : 0.0);
                                        double currentSpeed = algoItem != null ? algoItem.getSpeed() : 0.0;
                                        double multi = value / currentSpeed;
                                        double pastProfitability = multi * profitability;

                                        Double existing = chartItems.get(localDateTime);
                                        if (existing != null) {
                                            chartItems.put(localDateTime, existing + pastProfitability);
                                        } else {
                                            chartItems.put(localDateTime, pastProfitability);
                                        }
                                    }
                                }
                            }
                        }

                        LinkedList<ChartItem> tempData = new LinkedList<>();
                        for (Long key : chartItems.keySet()) {
                            Double y = chartItems.get(key);
                            tempData.add(new ChartItem(key / 1000, y != null ? y : 0.0));
                        }

                        subscriber.onSuccess(tempData);
                    }, throwable -> subscriber.onError(throwable));
        });
    }

    @Override
    public void getStatistics(String miner, long time) {
    }

    @Override
    public Single<List<ApiBalance>> getBalance(String miner) {
        return remoteMinerService.getStatistics("stats.provider.ex", miner)
                .flatMap(it -> {
                    JsonArray current = it.getResult().get("current").getAsJsonArray();
                    if (current == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiBalance> converted = new ArrayList<>(current.size());
                        for (JsonElement element : current) {
                            converted.add(gson.fromJson(element.getAsJsonObject(), ApiBalance.class));
                        }
                        return Single.just(converted);
                    }
                });
    }

    @Override
    public Single<List<ApiWorker>> getWorkers(String miner) {
        return remoteMinerService.getWorkers("stats.provider.workers", miner)
                .flatMap(it -> {
                    JsonArray workers = it.getResult().get("workers").getAsJsonArray();
                    if (workers == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiWorker> converted = new ArrayList<>(workers.size());
                        for (JsonElement element : workers) {
                            converted.add(new ApiWorker(element.getAsJsonArray().get(0).getAsString(),
                                    element.getAsJsonArray().get(2).getAsInt()));
                        }
                        return Single.just(converted);
                    }
                });
    }

    @Override
    public Single<List<ApiPayment>> getPayments(String miner) {
        return remoteMinerService.getStatistics("stats.provider", miner)
                .flatMap(it -> {
                    JsonArray payments = it.getResult().get("payments").getAsJsonArray();
                    if (payments == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiPayment> converted = new ArrayList<>(payments.size());
                        for (JsonElement element : payments) {
                            converted.add(gson.fromJson(element.getAsJsonObject(), ApiPayment.class));
                        }
                        return Single.just(converted);
                    }
                });
    }
}
