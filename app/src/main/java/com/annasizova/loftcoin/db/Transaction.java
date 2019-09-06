package com.annasizova.loftcoin.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

@AutoValue
@Entity(tableName = "transactions")
public abstract class Transaction implements StableId{

    @NonNull
    public static Transaction create(long id, long timestamp, double amount, long walletId) {
        return new AutoValue_Transaction(id, timestamp, amount, walletId);
    }

    @PrimaryKey(autoGenerate = true)
    @AutoValue.CopyAnnotations
    @Override
    public abstract long id();

    public abstract long timestamp();

    public abstract double amount();

    @ColumnInfo(name = "wallet_id")
    @AutoValue.CopyAnnotations
    public abstract long walletId();

    @AutoValue
    @DatabaseView(
            viewName = "transactions_view",
            value = "SELECT t.id, t.timestamp, t.wallet_id, c.symbol, " + "t.amount AS amount1, " +
                    "(c.price * t.amount) AS amount2 " + "FROM transactions AS t " +
                    "INNER JOIN wallets AS w ON t.wallet_id=w.id " + "INNER JOIN coins AS c ON w.coin_id=c.id"
    )

    public static abstract class View implements StableId {

        @NonNull
        public static Transaction.View create(long id, String symbol, long timestamp, double amount1, double amount2) {
            return new AutoValue_Transaction_View(id, symbol, timestamp, amount1, amount2);
        }

        @Override public abstract long id();

        public abstract String symbol();

        public abstract long timestamp();

        public abstract double amount1();

        public abstract double amount2();

    }
}
