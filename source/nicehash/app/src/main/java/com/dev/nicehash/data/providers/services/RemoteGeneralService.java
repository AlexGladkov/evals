package com.dev.nicehash.data.providers.services;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by Alex Gladkov on 05.08.18.
 * API calls for general functions
 */
public interface RemoteGeneralService {

    @GET("./api")
    Single<Object> getVersion();
}
