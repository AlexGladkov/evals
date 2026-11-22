package com.dev.nicehash.app.di;

import com.dev.nicehash.contracts.RemoteContract;
import com.dev.nicehash.data.providers.services.RemoteCurrencyService;
import com.dev.nicehash.data.providers.services.RemoteGeneralService;
import com.dev.nicehash.data.providers.services.RemoteMinerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RemoteModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(RemoteContract.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    RemoteCurrencyService provideRemoteCurrencyService(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(RemoteCurrencyService.class);
    }

    @Provides
    @Singleton
    RemoteGeneralService provideRemoteGeneralService(Retrofit retrofit) {
        return retrofit.create(RemoteGeneralService.class);
    }

    @Provides
    @Singleton
    RemoteMinerService provideRemoteMinerService(Retrofit retrofit) {
        return retrofit.create(RemoteMinerService.class);
    }
}
