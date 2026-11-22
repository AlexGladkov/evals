package com.dev.nicehash.data.converters;

import android.util.Log;

import com.dev.nicehash.data.room.models.MinerEntity;
import com.dev.nicehash.domain.models.Miner;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Implementation for converter interface
 * @see MinerConverter
 */
public class MinerConverterImpl implements MinerConverter {
    private final String TAG = MinerConverterImpl.class.getSimpleName();

    @Override
    public Miner dbToModel(MinerEntity minerEntity, int minerId) {
        return new Miner(minerEntity.getId() != null ? minerEntity.getId() : -1,
                minerEntity.getName(), minerEntity.getHash(),
                Objects.equals(minerEntity.getId(), minerId));
    }

    @Override
    public MinerEntity modelToDb(Miner miner) {
        return new MinerEntity(miner.getId(), miner.getName(), miner.getHash(), "");
    }
}
