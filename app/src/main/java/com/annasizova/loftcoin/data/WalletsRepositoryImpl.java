package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.db.LoftDB;
import com.annasizova.loftcoin.db.Transaction;
import com.annasizova.loftcoin.db.Wallet;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
class WalletsRepositoryImpl implements WalletsRepository {

    private final LoftDB db;

    @Inject
    WalletsRepositoryImpl(LoftDB db) {
        this.db = db;
    }

    @NonNull
    @Override
    public Observable<List<Wallet.View>> wallets() {
        return db.coins().coinsCount().flatMap(count -> db.wallets().fetchAllWallets());
    }

    @NonNull
    @Override
    public Observable<List<Transaction.View>> transactions(long walletId) {
        return db.wallets().fetchAllTransactions(walletId);
    }

    @NonNull
    @Override
    public Single<CoinEntity> findNextCoin() {
        return db.wallets().findNextCoin();
    }

    @NonNull
    @Override
    public Single<Long> saveWallet(Wallet wallet) {
        return db.wallets().insertWallet(wallet);
    }

    @NonNull
    @Override
    public Completable saveTransactions(List<Transaction> transactions) {
        return db.wallets().insertTransactions(transactions);
    }
}
