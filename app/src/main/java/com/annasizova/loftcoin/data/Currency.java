package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.R;

public enum Currency {

    USD(R.string.dollar, "$"),
    EUR(R.string.euro, "€"),
    RUB(R.string.ruble, "\u20BD");

    private final int nameResId;
    private final String sign;

    Currency(int nameResId, String sign) {
        this.nameResId = nameResId;
        this.sign = sign;
    }

    public int nameResId() {
        return nameResId;
    }

    @NonNull
    public String sign() {
        return sign;
    }

    @NonNull
    public String code() {
        return name();
    }
}

