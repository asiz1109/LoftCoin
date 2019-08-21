package com.annasizova.loftcoin.data;

import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName("price")
    double price;

    @SerializedName("percent_change_24h")
    double change24h;
}
