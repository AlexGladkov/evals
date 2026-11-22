package com.dev.nicehash.domain

import com.dev.nicehash.data.providers.models.ApiAlgo
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.helpers.MathUtils

/**
 * Pure, deterministic calculation of miner income from provider stats.
 *
 * Extracted verbatim from [com.dev.nicehash.data.implementations.MoneyRepositoryImpl]
 * (task C5) so that the calculation can be unit tested without Android, RxJava
 * or network. Time is injected via [calculate]'s `nowMillis` parameter instead of
 * reading the clock, which makes results deterministic.
 *
 * NOTE: the known quirks of the original implementation are intentionally
 * preserved exactly as they were (task is a refactoring, not a fix):
 *  - month income is a hardcoded average * 31 regardless of the real month length;
 *  - empty data yields NaN in every btc field (average of an empty collection);
 *  - a past entry whose algorithm has zero current speed divides by zero.
 */
class IncomeCalculator {

    /**
     * Calculates income periods for the given provider stats.
     * @param stats - provider stats response (current + past per-algorithm data)
     * @param nowMillis - current time in millis, used for the 24h window filter
     * @return income model with per-period btc values
     */
    fun calculate(stats: ApiProviderEx, nowMillis: Long): Income {
        val profitabilityItems = LinkedHashMap<Int, ApiAlgo>()
        val chartItems = LinkedHashMap<Long, Double>()

        stats.current.forEach { current ->
            profitabilityItems[current.algo] = ApiAlgo(speed = current.data.a.toDouble(),
                    price = current.profitability)
        }

        stats.past.forEach { past ->
            past.data.forEach { data ->
                val timestamp = data.time * 300
                val localDateTime = timestamp * 1000L

                val speedObject = data.a
                val profitability = (profitabilityItems[past.algo]?.price ?: 0.0) *
                        (profitabilityItems[past.algo]?.speed ?: 0.0)
                val currentSpeed = profitabilityItems[past.algo]?.speed ?: 0.0
                val multi = speedObject / currentSpeed
                val pastProfitability = multi * profitability

                if (chartItems.containsKey(localDateTime)) {
                    chartItems[localDateTime] = (chartItems[localDateTime] ?: 0.0) + pastProfitability
                } else {
                    chartItems[localDateTime] = pastProfitability
                }
            }
        }

        val dayDuration = nowMillis - 24 * 60 * 60 * 1000L
        val dayArray = chartItems.filter { it.key > dayDuration }


        return Income(hourValue = 0.0,
                hourBtc = MathUtils.getAverage(collection = dayArray.values) / 24, dayValue = 0.0,
                dayBtc = MathUtils.getAverage(collection = dayArray.values), monthValue = 0.0,
                monthBtc = MathUtils.getAverage(collection = dayArray.values) * 31,
                weekValue = 0.0, weekBtc = MathUtils.getAverage(collection = dayArray.values) * 7)
    }
}
