package com.annasizova.loftcoin.db;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Transaction implements StableId<String> {

    @NonNull
    public static Transaction create(String id, Double amount, Date timestamp, Wallet wallet) {
        return new AutoValue_Transaction(id, amount, timestamp, wallet);
    }

    public abstract double amount1();

    public abstract Date timestamp();

    public abstract Wallet wallet();

    public double amount2() {
        return amount1() * wallet().coin().price();
    }
}
