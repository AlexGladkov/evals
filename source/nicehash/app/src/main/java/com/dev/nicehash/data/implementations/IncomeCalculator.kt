package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.models.ApiAlgo
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.helpers.MathUtils
import org.joda.time.DateTime

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Pure income calculation from provider stats (no network / scheduling involved).
 * Extracted from MoneyRepositoryImpl to keep repository focused on data fetching (SRP).
 */
class IncomeCalculator {

    /**
     * Miner income for week, day, month based on past provider stats
     * @param result - provider ex stats fetched from remote
     * @return income model with btc rates (value fields are zero until converted to currency)
     */
    fun calculate(result: ApiProviderEx): Income {
        val profitabilityItems = LinkedHashMap<Int, ApiAlgo>()
        val chartItems = LinkedHashMap<Long, Double>()

        result.current.forEach { current ->
            profitabilityItems[current.algo] = ApiAlgo(speed = current.data.a.toDouble(),
                    price = current.profitability)
        }

        result.past.forEach { past ->
            past.data.forEach { data ->
                val timestamp = data.time * 300
                val localDateTime = DateTime(timestamp * 1000).millis

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

        val dayDuration = DateTime().minusDays(1).millis
        val dayArray = chartItems.filter { it.key > dayDuration }

        return Income(hourValue = 0.0,
                hourBtc = MathUtils.getAverage(collection = dayArray.values) / 24, dayValue = 0.0,
                dayBtc = MathUtils.getAverage(collection = dayArray.values), monthValue = 0.0,
                monthBtc = MathUtils.getAverage(collection = dayArray.values) * 31,
                weekValue = 0.0, weekBtc = MathUtils.getAverage(collection = dayArray.values) * 7)
    }
}
