package com.dev.nicehash.data.providers

import com.dev.nicehash.data.providers.models.ApiGlobal24
import com.dev.nicehash.data.providers.models.ApiGlobalCurrent
import com.dev.nicehash.data.providers.models.ApiPayments
import com.dev.nicehash.data.providers.models.ApiProvider
import com.dev.nicehash.data.providers.models.ApiProviderEx
import io.reactivex.Single

/**
 * Fake [NicehashProvider] for JVM unit tests.
 *
 * Returns the canned [ApiProviderEx] response given to the constructor and records the
 * arguments of the last [fetchStatsProviderEx] call so tests can assert on them.
 * The remaining endpoints are not used by
 * [com.dev.nicehash.data.implementations.MoneyRepositoryImpl] and fail fast.
 */
class FakeNicehashProvider(private val providerEx: ApiProviderEx) : NicehashProvider {

    var lastMiner: String? = null
        private set
    var lastFrom: Long = Long.MIN_VALUE
        private set

    override fun fetchGlobalCurrent(): Single<ApiGlobalCurrent> {
        return Single.error(UnsupportedOperationException("Not used in tests"))
    }

    override fun fetchGlobal24(): Single<ApiGlobal24> {
        return Single.error(UnsupportedOperationException("Not used in tests"))
    }

    override fun fetchStatsProvider(miner: String): Single<ApiProvider> {
        return Single.error(UnsupportedOperationException("Not used in tests"))
    }

    override fun fetchStatsProviderEx(miner: String, from: Long): Single<ApiProviderEx> {
        lastMiner = miner
        lastFrom = from
        return Single.just(providerEx)
    }

    override fun fetchStatsProviderPayments(miner: String, from: Long): Single<ApiPayments> {
        return Single.error(UnsupportedOperationException("Not used in tests"))
    }
}
