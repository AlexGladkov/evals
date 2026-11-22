package com.dev.nicehash.data.implementations;

import com.dev.nicehash.data.providers.GeneralProvider;
import com.dev.nicehash.domain.repositories.GeneralRepository;

/**
 * Created by Alex Gladkov on 05.08.18.
 * General repository implementation
 */
public class GeneralRepositoryImpl implements GeneralRepository {

    private final GeneralProvider generalProvider;

    public GeneralRepositoryImpl(GeneralProvider generalProvider) {
        this.generalProvider = generalProvider;
    }

    @Override
    public void getVersion() {
        generalProvider.getVersion();
    }
}
