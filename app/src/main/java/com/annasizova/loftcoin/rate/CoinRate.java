package com.annasizova.loftcoin.rate;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class CoinRate {

    @NonNull
    static CoinRate.Builder builder() {
        return new AutoValue_CoinRate.Builder();
    }

    abstract int id();
    abstract String symbol();
    abstract String price();
    abstract String change24h();
    abstract String imageUrl();
    abstract boolean isChange24hNegative();

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder id(int value);
        abstract Builder symbol(String value);
        abstract Builder price(String value);
        abstract Builder change24h(String value);
        abstract Builder imageUrl(String value);
        abstract Builder isChange24hNegative(boolean value);
        abstract CoinRate build();
    }
}
