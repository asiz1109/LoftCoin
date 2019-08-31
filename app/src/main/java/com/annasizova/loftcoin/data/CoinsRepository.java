package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.util.Consumer;

import java.util.List;

public interface CoinsRepository {

    void listings(@NonNull String convert, @NonNull Consumer<List<Coin>> onSuccess, @NonNull Consumer<Throwable> onError);

    LiveData<List<CoinEntity>> listings();

    void refresh(@NonNull String convert, @NonNull Runnable onSuccess, @NonNull Consumer<Throwable> onError);
}
