package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.util.Currency;
import java.util.Locale;

public interface Currencies {

    @NonNull
    Pair<Currency, Locale> getCurrent();
}
