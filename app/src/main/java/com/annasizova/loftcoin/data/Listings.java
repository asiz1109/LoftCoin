package com.annasizova.loftcoin.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Listings {

    @SerializedName("data")
    List<Coin> data;
}
