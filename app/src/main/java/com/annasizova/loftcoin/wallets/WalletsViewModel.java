package com.annasizova.loftcoin.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.data.WalletsRepository;
import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.db.Transaction;
import com.annasizova.loftcoin.db.Wallet;
import com.annasizova.loftcoin.rx.RxSchedulers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

class WalletsViewModel extends ViewModel {

    private static final Random PRNG = new SecureRandom();
    private final WalletsRepository repository;
    private final RxSchedulers schedulers;
    private final BehaviorSubject<Long> walletId = BehaviorSubject.create();

    @Inject
    WalletsViewModel(WalletsRepository repository, RxSchedulers schedulers) {
        this.repository = repository;
        this.schedulers = schedulers;
    }

    @NonNull
    Completable createNextWallet() {
        return repository.findNextCoin()
                .map(this::createFakeWallet)
                .flatMap(repository::saveWallet)
                .map(this::generateFakeTransactions)
                .flatMapCompletable(repository::saveTransactions)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main());
    }

    @NonNull
    Observable<List<Wallet.View>> wallets() {
        return repository.wallets().doOnNext(wallets -> {
                    final Long value = walletId.getValue();
                    if (value == null && !wallets.isEmpty()) {
                        walletId.onNext(wallets.get(0).id());
                    }
                })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main());
    }

    @NonNull
    Observable<List<Transaction.View>> transactions() {
        return walletId.distinctUntilChanged()
                .flatMap(repository::transactions)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main());
    }

    void submitWalletId(long id) {
        walletId.onNext(id);
    }

    private Wallet createFakeWallet(CoinEntity coin) {
        return Wallet.create(0, PRNG.nextDouble() * (1 + PRNG.nextInt(100)), coin.id());
    }

    private List<Transaction> generateFakeTransactions(long walletId) {
        final int count = 1 + PRNG.nextInt(20);
        final List<Transaction> transactions = new ArrayList<>(count);
        final long now = System.currentTimeMillis();
        for (int i = 0; i < count; ++i) {
            transactions.add(Transaction.create(0, now - TimeUnit.HOURS.toMillis(12 + PRNG.nextInt(120)), PRNG.nextDouble() * (PRNG.nextInt(100) - 50), walletId));
        }
        return transactions;
    }
}