package com.annasizova.loftcoin.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoinsDao {

    @Query("SELECT * FROM coins")
    LiveData<List<CoinEntity>> fetchAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CoinEntity> coins);
}
