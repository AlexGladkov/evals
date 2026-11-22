package com.dev.nicehash.data.providers.implementations;

import android.util.Log;

import com.dev.nicehash.data.providers.GeneralProvider;
import com.dev.nicehash.data.providers.services.RemoteGeneralService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 05.08.18.
 * Implementation for GeneralProvider interface
 */
public class GeneralProviderImpl implements GeneralProvider {
    private final String TAG = GeneralProviderImpl.class.getSimpleName();

    private final RemoteGeneralService remoteGeneralService;

    public GeneralProviderImpl(RemoteGeneralService remoteGeneralService) {
        this.remoteGeneralService = remoteGeneralService;
    }

    @Override
    public void getVersion() {
        remoteGeneralService.getVersion()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> Log.e(TAG, "result " + result),
                        error -> Log.e(TAG, "error " + error));
    }
}
