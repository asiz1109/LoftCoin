package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.db.CoinEntity;

import java.util.List;

import io.reactivex.Observable;

public interface CoinsRepository {

    @NonNull
    Observable<List<CoinEntity>> listings(@NonNull String convert);
}
