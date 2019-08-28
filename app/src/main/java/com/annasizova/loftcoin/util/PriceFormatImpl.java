package com.annasizova.loftcoin.util;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.annasizova.loftcoin.data.Currencies;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
public class PriceFormatImpl implements PriceFormat {

    private Currencies currencies;

    @Inject
    PriceFormatImpl(@NonNull Currencies currencies) {
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public String format(double value) {
        final Pair<Currency, Locale> pair = currencies.getCurrent();
        final Locale locale = Objects.requireNonNull(pair.second);
        return NumberFormat.getCurrencyInstance(locale).format(value);
    }

}
