package com.annasizova.loftcoin.data;

import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName("price")
    double price;

    @SerializedName("percent_change_24h")
    double change24h;

    public double getPrice() {
        return price;
    }

    public double getChange24h() {
        return change24h;
    }
}
