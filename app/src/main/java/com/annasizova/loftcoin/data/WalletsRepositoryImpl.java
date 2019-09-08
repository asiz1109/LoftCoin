package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.db.LoftDB;
import com.annasizova.loftcoin.db.Transaction;
import com.annasizova.loftcoin.db.Wallet;
import com.annasizova.loftcoin.rx.RxSchedulers;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;

@Singleton
class WalletsRepositoryImpl implements WalletsRepository {

    private final LoftDB db;
    private final RxSchedulers schedulers;
    private final FirebaseFirestore firestore;
    private final Executor ioExecutor;

    @Inject
    WalletsRepositoryImpl(RxSchedulers schedulers, LoftDB db) {
        this.schedulers = schedulers;
        this.db = db;
        firestore = FirebaseFirestore.getInstance();
        ioExecutor = schedulers.io()::scheduleDirect;
    }

    @NonNull
    @Override
    public Observable<List<Wallet>> wallets() {
        return Observable.create(new FirestoreOnSubscribe(ioExecutor, firestore
                .collection("wallets")
                .orderBy("created", Query.Direction.ASCENDING)))
                .flatMap(documents -> Observable
                        .fromIterable(documents)
                        .flatMapSingle(document -> db.coins()
                                .fetchCoin(document.getLong("coinId"))
                                .map(coin -> createWallet(document, coin))
                        )
                        .toList().toObservable()
                );
    }

    @NonNull
    @Override
    public Observable<List<Transaction>> transactions(@NonNull Wallet wallet) {
        return Observable.create(new FirestoreOnSubscribe(ioExecutor, firestore
                .collection("wallets").document(wallet.id())
                .collection("transactions")
                .orderBy("timestamp", Query.Direction.DESCENDING)))
                .flatMap(documents -> Observable.fromIterable(documents)
                        .map(document -> createTransaction(document, wallet))
                        .toList().toObservable()
                );
    }

    @NonNull
    @Override
    public Single<CoinEntity> findNextCoin(@NonNull List<Long> exclude) {
        return db.coins().findNextCoin(exclude);
    }

    @NonNull
    @Override
    public Completable saveWallet(Wallet wallet) {
        return Single.fromCallable(() -> {
            final Map<String, Object> data = new HashMap<>();
            data.put("balance", wallet.balance1());
            data.put("coinId", wallet.coin().id());
            data.put("symbol", wallet.coin().symbol());
            data.put("created", FieldValue.serverTimestamp());
            return data;
        }).flatMapCompletable(data -> Completable.create(emitter -> { firestore
                    .collection("wallets").add(data)
                    .addOnCompleteListener(ioExecutor, doc -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(ioExecutor, emitter::tryOnError);}))
                .subscribeOn(schedulers.io());
    }

    private Wallet createWallet(@Nullable DocumentSnapshot doc, @NonNull CoinEntity coin) {
        Objects.requireNonNull(doc, "doc is null?!");
        return Wallet.create(doc.getId(), doc.getDouble("balance"), coin);
    }

    private Transaction createTransaction(@Nullable DocumentSnapshot doc, @NonNull Wallet wallet) {
        Objects.requireNonNull(doc, "doc is null?!");
        return Transaction.create(doc.getId(), doc.getDouble("amount"), doc.getDate("timestamp"), wallet);
    }

    private static class FirestoreOnSubscribe implements ObservableOnSubscribe<List<DocumentSnapshot>> {

        private final Query query;
        private final Executor executor;

        FirestoreOnSubscribe(@NonNull Executor executor, @NonNull Query query) {
            this.executor = executor;
            this.query = query;
        }

        @Override
        public void subscribe(ObservableEmitter<List<DocumentSnapshot>> emitter) {
            final ListenerRegistration registration = query
                    .addSnapshotListener(executor, (snapshots, e) -> {
                        if (snapshots != null) {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(snapshots.getDocuments());
                            }
                        } else if (e != null) {
                            emitter.tryOnError(e);
                        }
                    });
            emitter.setCancellable(registration::remove);
        }
    }
}
