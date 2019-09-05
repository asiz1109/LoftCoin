package com.annasizova.loftcoin.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface WalletsDao {

    @Query("SELECT * FROM wallets_view")
    Observable<List<Wallet.View>> fetchAllWallets();

    @Query("SELECT * FROM transactions_view WHERE wallet_id=:walletId ORDER BY timestamp DESC")
    Observable<List<Transaction.View>> fetchAllTransactions(long walletId);

    @Query("SELECT * FROM coins " + "WHERE id NOT IN(SELECT coin_id FROM wallets) " +
            "ORDER BY price DESC " + "LIMIT 1")
    Single<CoinEntity> findNextCoin();

    @Insert
    Single<Long> insertWallet(Wallet wallet);

    @Insert
    Completable insertTransactions(List<Transaction> transactions);
}
