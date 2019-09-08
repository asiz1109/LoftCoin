package com.annasizova.loftcoin.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.data.WalletsRepository;
import com.annasizova.loftcoin.db.Transaction;
import com.annasizova.loftcoin.db.Wallet;
import com.annasizova.loftcoin.rx.RxSchedulers;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class WalletsViewModel extends ViewModel {

    private static final Random PRNG = new SecureRandom();
    private final WalletsRepository repository;
    private final RxSchedulers schedulers;
    private final Observable<List<Wallet>> wallets;
    private final Subject<Integer> walletPosition;
    private final Observable<List<Transaction>> transactions;

    @Inject
    WalletsViewModel(WalletsRepository repository, RxSchedulers schedulers) {
        this.repository = repository;
        this.schedulers = schedulers;
        wallets = repository.wallets()
                .replay(1)
                .autoConnect()
                .subscribeOn(schedulers.io());
        walletPosition = BehaviorSubject.createDefault(0);
        transactions = wallets
                .filter(wallets -> !wallets.isEmpty())
                .switchMap(wallets -> walletPosition
                        .observeOn(schedulers.io())
                        .map(position -> Math.max(0, position))
                        .map(position -> Math.min(position, wallets.size() - 1))
                        .map(wallets::get)
                )
                .distinctUntilChanged(Wallet::id)
                .switchMap(repository::transactions)
                .subscribeOn(schedulers.io());
    }

    @NonNull
    Completable createNextWallet() {
        return wallets.firstElement()
                .flatMapSingle(wallets -> Observable.fromIterable(wallets).map(wallet -> wallet.coin().id()).toList())
                .flatMap(repository::findNextCoin)
                .map(coin -> Wallet.create(UUID.randomUUID().toString(), PRNG.nextDouble() * (1 + PRNG.nextInt(100)), coin))
                .flatMapCompletable(repository::saveWallet)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main());
    }

    @NonNull
    Observable<List<Wallet>> wallets() {
        return wallets.observeOn(schedulers.main());
    }

    @NonNull
    Observable<List<Transaction>> transactions() {
        return transactions.observeOn(schedulers.main());
    }

    void submitWalletPosition(int position) {
        walletPosition.onNext(position);
    }
}