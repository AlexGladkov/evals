package com.dev.nicehash.data.providers.implementations;

import android.util.Log;

import com.dev.nicehash.data.providers.NicehashProvider;
import com.dev.nicehash.data.providers.models.ApiGlobal24;
import com.dev.nicehash.data.providers.models.ApiGlobal24Stats;
import com.dev.nicehash.data.providers.models.ApiGlobalCurrent;
import com.dev.nicehash.data.providers.models.ApiGlobalCurrentStats;
import com.dev.nicehash.data.providers.models.ApiPayment;
import com.dev.nicehash.data.providers.models.ApiPayments;
import com.dev.nicehash.data.providers.models.ApiPaymentsData;
import com.dev.nicehash.data.providers.models.ApiProvider;
import com.dev.nicehash.data.providers.models.ApiProviderCurrent;
import com.dev.nicehash.data.providers.models.ApiProviderData;
import com.dev.nicehash.data.providers.models.ApiProviderEx;
import com.dev.nicehash.data.providers.models.ApiProviderPast;
import com.dev.nicehash.data.providers.models.ApiProviderPastData;
import com.dev.nicehash.data.providers.models.ApiProviderStats;
import com.dev.nicehash.data.providers.services.RemoteMinerService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Implementation for NiceHash provider interface
 */
public class NicehashProviderImpl implements NicehashProvider {
    private final String TAG = NicehashProviderImpl.class.getSimpleName();
    private final Gson gson = new Gson();

    private long lastProviderEx = 0L;

    private final RemoteMinerService remoteMinerService;

    public NicehashProviderImpl(RemoteMinerService remoteMinerService) {
        this.remoteMinerService = remoteMinerService;
    }

    /**
     * Get current profitability (price) and hashing speed for all algorithms. Refreshed every 30 seconds.
     */
    @Override
    public Single<ApiGlobalCurrent> fetchGlobalCurrent() {
        return remoteMinerService.getStatsGlobalCurrent()
                .flatMap(it -> {
                    JsonElement statsEl = it.getResult().get("stats");
                    JsonArray stats = statsEl != null ? statsEl.getAsJsonArray() : null;
                    if (stats == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiGlobalCurrentStats> list = new LinkedList<>();
                        for (JsonElement element : stats) {
                            list.add(gson.fromJson(element, ApiGlobalCurrentStats.class));
                        }
                        return Single.just(new ApiGlobalCurrent(it.getMethod(), list));
                    }
                });
    }

    /**
     * Get average profitability (price) and hashing speed for all algorithms in past 24 hours.
     */
    @Override
    public Single<ApiGlobal24> fetchGlobal24() {
        return remoteMinerService.getStatsGlobal24H()
                .flatMap(it -> {
                    JsonElement statsEl = it.getResult().get("stats");
                    JsonArray stats = statsEl != null ? statsEl.getAsJsonArray() : null;
                    if (stats == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiGlobal24Stats> list = new LinkedList<>();
                        for (JsonElement element : stats) {
                            list.add(gson.fromJson(element, ApiGlobal24Stats.class));
                        }
                        return Single.just(new ApiGlobal24(it.getMethod(), list));
                    }
                });
    }

    /**
     * Get current stats for provider for all algorithms. Refreshed every 30 seconds. It also returns past 56 payments.
     * @param miner - Provider's BTC address
     */
    @Override
    public Single<ApiProvider> fetchStatsProvider(String miner) {
        return remoteMinerService.getStatsProvider("stats.provider", miner)
                .flatMap(it -> {
                    JsonElement statsEl = it.getResult().get("stats");
                    JsonArray stats = statsEl != null ? statsEl.getAsJsonArray() : null;
                    if (stats == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiProviderStats> list = new LinkedList<>();
                        for (JsonElement element : stats) {
                            list.add(gson.fromJson(element, ApiProviderStats.class));
                        }
                        return Single.just(new ApiProvider(it.getMethod(), list));
                    }
                });
    }

    /**
     * Get detailed stats for provider for all algorithms including history data and payments in past 7 days.
     * @param miner - Provider's BTC address
     * @param from - Get history data from this time (UNIX timestamp). This parameter is optional and is by default considered to be 0 (return complete history).
     */
    @Override
    public Single<ApiProviderEx> fetchStatsProviderEx(String miner, long from) {
        return remoteMinerService.getStatsProviderEx("stats.provider.ex", miner, from)
                .cache()
                .flatMap(it -> {
                    JsonElement currentEl = it.getResult().get("current");
                    JsonArray current = currentEl != null ? currentEl.getAsJsonArray() : null;
                    JsonElement pastEl = it.getResult().get("past");
                    JsonArray past = pastEl != null ? pastEl.getAsJsonArray() : null;
                    JsonElement paymentsEl = it.getResult().get("payments");
                    JsonArray payments = paymentsEl != null ? paymentsEl.getAsJsonArray() : null;
                    JsonElement addrEl = it.getResult().get("addr");
                    String addr = addrEl != null ? addrEl.getAsString() : null;
                    if (current == null || past == null || payments == null || addr == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiProviderCurrent> currentData = new LinkedList<>();
                        List<ApiProviderPast> pastData = new LinkedList<>();
                        for (JsonElement element : current) {
                            JsonArray dataItem = element.getAsJsonObject().get("data").getAsJsonArray();
                            JsonElement rsEl = dataItem.get(0).getAsJsonObject().get("rs");
                            currentData.add(new ApiProviderCurrent(
                                    element.getAsJsonObject().get("profitability").getAsDouble(),
                                    element.getAsJsonObject().get("suffix").getAsString(),
                                    element.getAsJsonObject().get("name").getAsString(),
                                    element.getAsJsonObject().get("algo").getAsInt(),
                                    new ApiProviderData(
                                            dataItem.get(0).getAsJsonObject().get("a").getAsFloat(),
                                            rsEl != null ? rsEl.getAsFloat() : 0f,
                                            dataItem.get(1).getAsDouble())));
                        }

                        for (JsonElement element : past) {
                            List<ApiProviderPastData> data = new LinkedList<>();
                            JsonArray jsonData = element.getAsJsonObject().get("data").getAsJsonArray();

                            try {
                                for (JsonElement dataElem : jsonData) {
                                    data.add(new ApiProviderPastData(
                                            dataElem.getAsJsonArray().get(0).getAsLong(),
                                            dataElem.getAsJsonArray().get(1).getAsJsonObject().get("a").getAsFloat(),
                                            dataElem.getAsJsonArray().get(2).getAsDouble()));
                                }
                            } catch (Exception e) {
                                // TODO smth
                            }

                            pastData.add(new ApiProviderPast(element.getAsJsonObject().get("algo").getAsInt(),
                                    data));
                        }

                        return Single.just(new ApiProviderEx(it.getMethod(), currentData,
                                pastData, new LinkedList<ApiPayment>(), addr));
                    }
                });
    }

    /**
     * Get payments for provider.
     * @param miner - Provider's BTC address
     * @param from - Get history data from this time (UNIX timestamp). This parameter is optional and is by default considered to be 0 (return complete history).
     */
    @Override
    public Single<ApiPayments> fetchStatsProviderPayments(String miner, long from) {
        return remoteMinerService.getStatsProviderPayments("stats.provider.payments", miner, from)
                .flatMap(it -> {
                    JsonElement paymentsEl = it.getResult().get("payments");
                    JsonArray payments = paymentsEl != null ? paymentsEl.getAsJsonArray() : null;
                    JsonElement addrEl = it.getResult().get("addr");
                    String addr = addrEl != null ? addrEl.getAsString() : null;
                    JsonElement nhWalletEl = it.getResult().get("nh_wallet");
                    Boolean nh_wallet = nhWalletEl != null ? nhWalletEl.getAsBoolean() : null;
                    if (payments == null || addr == null || nh_wallet == null) {
                        return Single.error(new NullPointerException());
                    } else {
                        List<ApiPaymentsData> list = new LinkedList<>();
                        for (JsonElement element : payments) {
                            list.add(gson.fromJson(element, ApiPaymentsData.class));
                        }
                        return Single.just(new ApiPayments(it.getMethod(), list, nh_wallet, addr));
                    }
                });
    }
}
