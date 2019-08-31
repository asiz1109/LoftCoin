package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.db.LoftDB;
import com.annasizova.loftcoin.util.Consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
class CoinsRepositoryImpl implements CoinsRepository {

    private final CoinMarketCapApi cmcApi;
    private final LoftDB db;

    @Inject
    CoinsRepositoryImpl(CoinMarketCapApi api, LoftDB db) {
        cmcApi = api;
        this.db = db;
    }

    @Override
    public void listings(@NonNull String convert, @NonNull Consumer<List<Coin>> onSuccess, @NonNull Consumer<Throwable> onError) {
        cmcApi.listings(convert).enqueue(new Callback<Listings>() {
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

    @Override
    public LiveData<List<CoinEntity>> listings() {
        return db.coins().fetchAll();
    }

    @Override
    public void refresh(@NonNull String convert, @NonNull Runnable onSuccess, @NonNull Consumer<Throwable> onError) {
        listings(convert, coins -> {
            final List<CoinEntity> entities = new ArrayList<>();
            for (final Coin coin : coins) {
                double price = 0d;
                double change24 = 0d;
                final Quote quote = coin.getQuotes().get(convert);
                if (quote != null) {
                    price = quote.getPrice();
                    change24 = quote.getChange24h();
                }
                entities.add(CoinEntity.create(coin.getId(), coin.getSymbol(), price, change24));
            }
            db.coins().insertAll(entities);
            onSuccess.run();
        }, onError);
    }
}
