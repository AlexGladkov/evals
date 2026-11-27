package com.dev.nicehash.data.providers

import com.dev.nicehash.data.providers.models.ApiGlobal24
import com.dev.nicehash.data.providers.models.ApiGlobalCurrent
import com.dev.nicehash.data.providers.models.ApiPayments
import com.dev.nicehash.data.providers.models.ApiProvider
import com.dev.nicehash.data.providers.models.ApiProviderEx
import io.reactivex.Single

/**
 * Fake [NicehashProvider] for unit tests. Returns canned [ApiProviderEx] data
 * and fails fast on any other endpoint.
 */
class FakeNicehashProvider(private val providerEx: ApiProviderEx) : NicehashProvider {

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
        return Single.just(providerEx)
    }

    override fun fetchStatsProviderPayments(miner: String, from: Long): Single<ApiPayments> {
        return Single.error(UnsupportedOperationException("Not used in tests"))
    }
}
