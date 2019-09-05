package com.annasizova.loftcoin.data;

import com.annasizova.loftcoin.BuildConfig;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public interface DataModule {

    @Provides
    @Singleton
    static OkHttpClient httpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new CoinMarketCapApi.AddKeyInterceptor());
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.redactHeader(CoinMarketCapApi.KEY_HEADER);
            httpClient.addInterceptor(interceptor);
        }
        return httpClient.build();
    }

    @Provides
    @Singleton
    static CoinMarketCapApi coinMarketCapApi(OkHttpClient httpClient) {
        final Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BuildConfig.CMC_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callbackExecutor(Executors.newFixedThreadPool(4))
                .build();
        return retrofit.create(CoinMarketCapApi.class);
    }

    @Binds
    CoinsRepository coinsRepository(CoinsRepositoryImpl impl);

    @Binds
    WalletsRepository walletsRepository(WalletsRepositoryImpl impl);

    @Binds
    Currencies currencies(CurrenciesImpl impl);
}
