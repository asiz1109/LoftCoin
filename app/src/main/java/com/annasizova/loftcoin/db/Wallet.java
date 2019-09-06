package com.annasizova.loftcoin.db;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Wallet implements StableId<String> {

    @NonNull
    public static Wallet create(String id, double balance, CoinEntity coin) {
        return new AutoValue_Wallet(id, balance, coin);
    }

    public abstract double balance1();

    public abstract CoinEntity coin();

    public double balance2() {
        return balance1() * coin().price();
    }
}
