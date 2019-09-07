package com.annasizova.loftcoin.util;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Tuple<F, S> {

    @NonNull
    public static <F, S> Tuple<F, S> create(F first, S second) {
        return new AutoValue_Tuple<>(first, second);
    }

    public abstract F first();

    public abstract S second();
}
