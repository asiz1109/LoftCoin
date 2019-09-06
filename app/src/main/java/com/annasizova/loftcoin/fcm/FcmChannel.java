package com.annasizova.loftcoin.fcm;

import androidx.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface FcmChannel {

    @NonNull
    Single<String> token();

    @NonNull
    Completable createDefaultChannel();

    @NonNull
    Completable notify(@NonNull String title, @NonNull String message, @NonNull Class<?> receiver);
}
