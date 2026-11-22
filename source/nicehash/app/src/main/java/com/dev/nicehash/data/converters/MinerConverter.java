package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.room.models.MinerEntity;
import com.dev.nicehash.domain.models.Miner;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Interface for miner converter
 */
public interface MinerConverter {
    Miner dbToModel(MinerEntity minerEntity, int minerId);
    MinerEntity modelToDb(Miner miner);
}
