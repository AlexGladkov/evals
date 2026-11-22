package com.dev.nicehash.data.providers;

import com.dev.nicehash.data.providers.models.ApiGlobal24;
import com.dev.nicehash.data.providers.models.ApiGlobalCurrent;
import com.dev.nicehash.data.providers.models.ApiPayments;
import com.dev.nicehash.data.providers.models.ApiProvider;
import com.dev.nicehash.data.providers.models.ApiProviderEx;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Provider for all api of NiceHash service
 */
public interface NicehashProvider {
    Single<ApiGlobalCurrent> fetchGlobalCurrent();
    Single<ApiGlobal24> fetchGlobal24();
    Single<ApiProvider> fetchStatsProvider(String miner);
    Single<ApiProviderEx> fetchStatsProviderEx(String miner, long from);
    Single<ApiPayments> fetchStatsProviderPayments(String miner, long from);
}
