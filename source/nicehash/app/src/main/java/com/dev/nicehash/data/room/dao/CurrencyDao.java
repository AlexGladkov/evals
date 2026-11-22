package com.dev.nicehash.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dev.nicehash.contracts.RoomContract;
import com.dev.nicehash.data.room.models.ExchangeRateEntity;

import java.util.List;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Dao interface for Room Currency Entity
 */
@Dao
public interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long updateCurrency(ExchangeRateEntity currencyEntity);

    @Query("SELECT * FROM " + RoomContract.TABLE_CURRENCY)
    List<ExchangeRateEntity> fetchCurrencies();
}
