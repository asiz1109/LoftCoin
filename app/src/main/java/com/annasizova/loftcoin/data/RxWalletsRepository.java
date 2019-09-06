package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

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
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;

@Singleton
class RxWalletsRepository implements WalletsRepository {

    private final LoftDB loftDb;
    private final FirebaseFirestore firestore;
    private final Executor ioExecutor;

    @Inject
    RxWalletsRepository(RxSchedulers schedulers, LoftDB loftDb) {
        this.loftDb = loftDb;
        firestore = FirebaseFirestore.getInstance();
        ioExecutor = schedulers.io()::scheduleDirect;
    }

    @NonNull
    @Override
    public Observable<List<Wallet>> wallets() {
        return Observable.create(new QueryOnSubscribe(ioExecutor, firestore
                    .collection("wallets")
                    .orderBy("created", Query.Direction.ASCENDING)))
                .flatMapSingle(documents -> Observable
                        .fromIterable(documents)
                        .flatMapSingle(document -> loftDb.coins().fetchCoin(document.getLong("coinId"))
                                .map(coin -> Wallet.create(document.getId(), document.getDouble("balance"), coin)))
                        .toList());
    }

    @NonNull
    @Override
    public Observable<List<Transaction>> transactions(@NonNull Wallet wallet) {
        return Observable.create(new QueryOnSubscribe(ioExecutor, firestore
                        .collection("wallets")
                        .document(wallet.id())
                        .collection("transactions")
                        .orderBy("timestamp", Query.Direction.DESCENDING)))
                .flatMapSingle(documents -> Observable
                        .fromIterable(documents)
                        .map(document -> Transaction.create(document.getId(), document.getDouble("amount"), document.getDate("timestamp"), wallet))
                        .toList());
    }

    @NonNull
    @Override
    public Single<CoinEntity> findNextCoin(@NonNull List<Long> exclude) {
        return loftDb.coins().findNextCoin(exclude);
    }

    @NonNull
    @Override
    public Completable saveWallet(Wallet wallet) {
        return Completable.create(emitter -> {
            final Map<String, Object> data = new HashMap<>();
            data.put("balance", wallet.balance1());
            data.put("coinId", wallet.coin().id());
            data.put("symbol", wallet.coin().symbol());
            data.put("created", FieldValue.serverTimestamp());
            firestore.collection("wallets").add(data)
                    .addOnSuccessListener(ioExecutor, doc -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(ioExecutor, emitter::tryOnError);
        });
    }

    private static class QueryOnSubscribe implements ObservableOnSubscribe<List<DocumentSnapshot>> {

        private final Executor executor;
        private final Query query;

        QueryOnSubscribe(Executor executor, Query query) {
            this.executor = executor;
            this.query = query;
        }

        @Override
        public void subscribe(ObservableEmitter<List<DocumentSnapshot>> emitter) throws Exception {
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
