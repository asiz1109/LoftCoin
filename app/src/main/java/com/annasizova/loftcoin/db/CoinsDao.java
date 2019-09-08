package com.annasizova.loftcoin.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface CoinsDao {

    @Query("SELECT * FROM coins ORDER BY price DESC")
    Observable<List<CoinEntity>> fetchAll();

    @Query("SELECT * FROM coins ORDER BY price DESC LIMIT :limit")
    Observable<List<CoinEntity>> fetchCoins(int limit);

    @Query("SELECT * FROM coins WHERE id=:id")
    Single<CoinEntity> fetchCoin(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CoinEntity> coins);

    @Query("SELECT * FROM coins " + "WHERE id NOT IN(:exclude) " + "ORDER BY price DESC " + "LIMIT 1")
    Single<CoinEntity> findNextCoin(List<Long> exclude);
}
