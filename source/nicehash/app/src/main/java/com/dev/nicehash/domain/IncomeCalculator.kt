package com.dev.nicehash.domain

import com.dev.nicehash.data.providers.models.ApiAlgo
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.helpers.MathUtils
import org.joda.time.DateTime

/**
 * Pure, deterministic calculation of miner income from provider stats.
 *
 * Extracted verbatim from [com.dev.nicehash.data.implementations.MoneyRepositoryImpl]
 * (task C5) so that the calculation can be unit tested without Android, RxJava
 * or network. Time is injected via [calculate]'s `nowMillis` parameter instead of
 * reading the clock, which makes results deterministic.
 *
 * Behavior of the original implementation combined with the task/c2 bug fixes
 * (integration rule "c5 architecture + c2 math"):
 *  - month income uses the real number of days in the current month
 *    (derived from nowMillis, no more hardcoded 31);
 *  - empty data yields 0.0 (MathUtils.getAverage has an empty-guard since c2);
 *  - a past entry whose algorithm has zero current speed is skipped
 *    instead of dividing by zero.
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
                // zero current speed would turn the bucket into Infinity/NaN - skip it (fixed in c2)
                if (currentSpeed == 0.0) return@forEach
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

        // real number of days in the current month instead of hardcoded 31 (fixed in c2)
        val monthDays = DateTime(nowMillis).dayOfMonth().getMaximumValue()

        return Income(hourValue = 0.0,
                hourBtc = MathUtils.getAverage(collection = dayArray.values) / 24, dayValue = 0.0,
                dayBtc = MathUtils.getAverage(collection = dayArray.values), monthValue = 0.0,
                monthBtc = MathUtils.getAverage(collection = dayArray.values) * monthDays,
                weekValue = 0.0, weekBtc = MathUtils.getAverage(collection = dayArray.values) * 7)
    }
}
