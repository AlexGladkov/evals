package com.dev.nicehash.domain.repositories;

import com.dev.nicehash.domain.models.Income;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Repository for API linked with money and DB
 */
public interface MoneyRepository {
    Single<Income> fetchMinerIncome(String miner);
    Single<Double> fetchUnpaidMoney(String miner);
}
