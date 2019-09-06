package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.db.LoftDB;
import com.annasizova.loftcoin.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
class CoinsRepositoryImpl implements CoinsRepository {

    private final CoinMarketCapApi api;
    private final LoftDB db;
    private final RxSchedulers schedulers;

    @Inject
    CoinsRepositoryImpl(CoinMarketCapApi api, LoftDB db, RxSchedulers schedulers) {
        this.api = api;
        this.db = db;
        this.schedulers = schedulers;
    }

    @NonNull
    @Override
    public Observable<List<CoinEntity>> listings(@NonNull String convert) {
        return Observable.merge(
                db.coins().fetchAll(),
                api.listings(convert).map(this::fromListings).doOnNext(db.coins()::insertAll).skip(1).subscribeOn(schedulers.io())
        );
    }

    private List<CoinEntity> fromListings(Listings listings) {
        if (listings != null && listings.data != null) {
            final List<CoinEntity> entities = new ArrayList<>();
            for (final Coin coin : listings.data) {
                double price = 0d;
                double change24 = 0d;
                final Iterator<Quote> quotes = coin.getQuotes().values().iterator();
                if (quotes.hasNext()) {
                    final Quote quote = quotes.next();
                    if (quote != null) {
                        price = quote.getPrice();
                        change24 = quote.getChange24h();
                    }
                }
                entities.add(CoinEntity.create(coin.getId(), coin.getSymbol(), price, change24));
            }
            return Collections.unmodifiableList(entities);
        }
        return Collections.emptyList();
    }

}