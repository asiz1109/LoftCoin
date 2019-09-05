package com.annasizova.loftcoin.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CoinEntity.class, Wallet.class, Transaction.class}, views = {Wallet.View.class, Transaction.View.class}, version = 2)
public abstract class LoftDB extends RoomDatabase {

    public abstract CoinsDao coins();

    public abstract WalletsDao wallets();
}
