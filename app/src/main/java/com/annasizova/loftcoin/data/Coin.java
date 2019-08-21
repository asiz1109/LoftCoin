package com.annasizova.loftcoin.data;

import com.google.gson.annotations.SerializedName;

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
}
