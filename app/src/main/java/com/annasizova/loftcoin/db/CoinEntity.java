package com.annasizova.loftcoin.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

@AutoValue
@Entity (tableName = "coins")
public abstract class CoinEntity implements StableId<Long> {

    @PrimaryKey
    @AutoValue.CopyAnnotations
    @Override
    public abstract Long id();

    public abstract String symbol();

    public abstract double price();

    public abstract double change24();

    public static CoinEntity create(long id, String symbol, double price, double change24) {
        return new AutoValue_CoinEntity(id, symbol, price, change24);
    }
}
