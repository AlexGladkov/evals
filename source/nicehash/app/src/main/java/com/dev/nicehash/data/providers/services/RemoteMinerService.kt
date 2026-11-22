package com.dev.nicehash.data.providers.services

import com.dev.nicehash.contracts.RemoteContract
import com.dev.nicehash.data.providers.models.ApiGlobalCurrent
import com.dev.nicehash.data.providers.models.ApiResponse
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by Alex Gladkov on 05.08.18.
 * API calls for miner params
 */
interface RemoteMinerService {

    @GET("./api?method=stats.global.current")
    fun getStatsGlobalCurrent(): Single<ApiResponse>

    @GET("./api?method=stats.global.24h")
    fun getStatsGlobal24H(): Single<ApiResponse>

    @GET("./api")
    fun getStatsProvider(@Query(RemoteContract.METHOD) method: String = RemoteContract.METHOD_STATS_PROVIDER,
                         @Query(RemoteContract.MINER) miner: String): Single<ApiResponse>

    @GET("./api")
    fun getStatsProviderEx(@Query(RemoteContract.METHOD) method: String = RemoteContract.METHOD_STATS_PROVIDER_EX,
                           @Query(RemoteContract.MINER) miner: String,
                           @Query(RemoteContract.FROM) from: Long): Single<ApiResponse>

    @GET("./api")
    fun getStatsProviderPayments(@Query(RemoteContract.METHOD) method: String = RemoteContract.METHOD_STATS_PROVIDER_PAYMENTS,
                                 @Query(RemoteContract.MINER) miner: String,
                                 @Query(RemoteContract.FROM) from: Long): Single<ApiResponse>


    @GET("./api")
    fun getStatistics(@Query(RemoteContract.METHOD) method: String,
                      @Query(RemoteContract.MINER) miner: String): Single<ApiResponse>

    @GET("./api")
    fun getWorkers(@Query(RemoteContract.METHOD) method: String,
                   @Query(RemoteContract.MINER) miner: String): Single<ApiResponse>

    @GET("./api")
    fun getWorkerHistory(@Query(RemoteContract.METHOD) method: String,
                         @Query(RemoteContract.MINER) miner: String,
                         @Query(RemoteContract.FROM) from: Long): Single<ApiResponse>
}