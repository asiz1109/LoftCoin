package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.util.Consumer;

import java.util.List;

public interface CoinsRepository {

    @NonNull
    static CoinsRepository get() {
        return CoinsRepositoryImpl.get();
    }

    void listings(@NonNull String convert, @NonNull Consumer<List<Coin>> onSuccess, @NonNull Consumer<Throwable> onError);
}
