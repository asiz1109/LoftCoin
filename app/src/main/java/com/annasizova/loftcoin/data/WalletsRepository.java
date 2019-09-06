package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.db.Transaction;
import com.annasizova.loftcoin.db.Wallet;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface WalletsRepository {

    @NonNull
    Observable<List<Wallet>> wallets();

    @NonNull
    Observable<List<Transaction>> transactions(@NonNull Wallet wallet);

    @NonNull
    Single<CoinEntity> findNextCoin(@NonNull List<Long> exclude);

    @NonNull
    Completable saveWallet(Wallet wallet);

}
