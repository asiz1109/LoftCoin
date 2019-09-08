package com.annasizova.loftcoin.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CoinEntity.class}, version = 4)
public abstract class LoftDB extends RoomDatabase {

    public abstract CoinsDao coins();
}
