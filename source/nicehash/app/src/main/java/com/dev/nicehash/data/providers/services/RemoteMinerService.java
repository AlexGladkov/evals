package com.dev.nicehash.data.providers.services;

import com.dev.nicehash.contracts.RemoteContract;
import com.dev.nicehash.data.providers.models.ApiResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alex Gladkov on 05.08.18.
 * API calls for miner params
 */
public interface RemoteMinerService {

    @GET("./api?method=stats.global.current")
    Single<ApiResponse> getStatsGlobalCurrent();

    @GET("./api?method=stats.global.24h")
    Single<ApiResponse> getStatsGlobal24H();

    @GET("./api")
    Single<ApiResponse> getStatsProvider(@Query(RemoteContract.METHOD) String method,
                                         @Query(RemoteContract.MINER) String miner);

    @GET("./api")
    Single<ApiResponse> getStatsProviderEx(@Query(RemoteContract.METHOD) String method,
                                           @Query(RemoteContract.MINER) String miner,
                                           @Query(RemoteContract.FROM) long from);

    @GET("./api")
    Single<ApiResponse> getStatsProviderPayments(@Query(RemoteContract.METHOD) String method,
                                                 @Query(RemoteContract.MINER) String miner,
                                                 @Query(RemoteContract.FROM) long from);

    @GET("./api")
    Single<ApiResponse> getStatistics(@Query(RemoteContract.METHOD) String method,
                                      @Query(RemoteContract.MINER) String miner);

    @GET("./api")
    Single<ApiResponse> getWorkers(@Query(RemoteContract.METHOD) String method,
                                   @Query(RemoteContract.MINER) String miner);

    @GET("./api")
    Single<ApiResponse> getWorkerHistory(@Query(RemoteContract.METHOD) String method,
                                         @Query(RemoteContract.MINER) String miner,
                                         @Query(RemoteContract.FROM) long from);
}
