package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.db.LoftDB;
import com.annasizova.loftcoin.rx.RxSchedulers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
class CoinsRepositoryImpl implements CoinsRepository {

    private final CoinMarketCapApi api;
    private final LoftDB db;
    private final RxSchedulers schedulers;
    private final CoinsMapper mapper;
    private final Currencies currencies;

    @Inject
    CoinsRepositoryImpl(CoinMarketCapApi api, LoftDB db, RxSchedulers schedulers, CoinsMapper mapper, Currencies currencies) {
        this.api = api;
        this.db = db;
        this.schedulers = schedulers;
        this.mapper = mapper;
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public Observable<List<CoinEntity>> listings(@NonNull String convert) {
        return api.listings(convert)
                .map(mapper::apply)
                .doOnNext(db.coins()::insertAll)
                .switchMap(coins -> db.coins().fetchAll())
                .onErrorResumeNext(e -> {
                    return db.coins().fetchAll().flatMap(coins -> {
                        if (!coins.isEmpty()) return Observable.just(coins);
                        else return Observable.<List<CoinEntity>>error(e);
                    });
                })
                .subscribeOn(schedulers.io());
    }

    @NonNull
    @Override
    public Observable<List<CoinEntity>> top(int limit) {
        return db.coins().fetchCoins(limit);
    }
}