package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.BuildConfig;
import com.annasizova.loftcoin.util.Consumer;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class CoinsRepositoryImpl implements CoinsRepository {

    private static volatile CoinsRepositoryImpl sInstance;
    private final CoinMarketCapApi api;

    @NonNull
    static CoinsRepositoryImpl get() {
        CoinsRepositoryImpl instance = sInstance;
        if (instance == null) {
            synchronized (CoinsRepositoryImpl.class) {
                instance = sInstance;
                if (instance == null) {
                    instance = sInstance = new CoinsRepositoryImpl();
                }
            }
        }
        return instance;
    }

    private CoinsRepositoryImpl() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new CoinMarketCapApi.AddKeyInterceptor());
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.redactHeader(CoinMarketCapApi.KEY_HEADER);
            httpClient.addInterceptor(interceptor);
        }
        final Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(BuildConfig.CMC_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(CoinMarketCapApi.class);
    }

    @Override
    public void listings(@NonNull String convert, @NonNull Consumer<List<Coin>> onSuccess, @NonNull Consumer<Throwable> onError) {
        api.listings(convert).enqueue(new Callback<Listings>() {
            @Override
            public void onResponse(Call<Listings> call, Response<Listings> response) {
                final Listings listings = response.body();
                if (listings != null && listings.data != null) {
                    onSuccess.apply(Collections.unmodifiableList(listings.data));
                } else {
                    onSuccess.apply(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<Listings> call, Throwable t) {
                onError.apply(t);
            }
        });
    }
}
