package com.dev.nicehash.app.di

import com.dev.nicehash.contracts.RemoteContract
import com.dev.nicehash.data.providers.services.ChatService
import com.dev.nicehash.data.providers.services.RemoteCurrencyService
import com.dev.nicehash.data.providers.services.RemoteMinerService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideGson(): Gson =
            GsonBuilder()
                    .create()

    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()

    // Single place for retrofit configuration to avoid duplicated builders (DRY)
    private fun buildRetrofit(baseUrl: String, gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()

    @Provides @Singleton fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            buildRetrofit(baseUrl = RemoteContract.baseUrl, gson = gson, okHttpClient = okHttpClient)

    @Provides
    @Singleton
    fun provideRemoteCurrencyService(gson: Gson, okHttpClient: OkHttpClient): RemoteCurrencyService =
            buildRetrofit(baseUrl = RemoteContract.baseUrlCurrency, gson = gson, okHttpClient = okHttpClient)
                    .create(RemoteCurrencyService::class.java)

    @Provides
    @Singleton
    fun provideRemoteMinerService(retrofit: Retrofit): RemoteMinerService =
            retrofit.create(RemoteMinerService::class.java)

    @Provides
    @Singleton
    fun provideChatService(gson: Gson, okHttpClient: OkHttpClient): ChatService {
        val retrofit = Retrofit.Builder()
                .baseUrl(RemoteContract.chatBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(ChatService::class.java)
    }
}
