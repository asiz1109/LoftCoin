package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Map;

public class Coin {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("symbol")
    String symbol;

    @SerializedName("quote")
    Map<String, Quote> quotes;

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name == null ? "" : name;
    }

    @NonNull
    public String getSymbol() {
        return symbol == null ? "" : symbol;
    }

    @NonNull
    public Map<String, Quote> getQuotes() {
        return quotes == null ? Collections.emptyMap() : Collections.unmodifiableMap(quotes);
    }
}
