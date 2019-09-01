package com.annasizova.loftcoin.util;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.annasizova.loftcoin.data.Currencies;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Reusable;

@Reusable
public class PriceFormatImpl implements PriceFormat {

    private Currencies currencies;
    private final Provider<Locale> locale;

    @Inject
    PriceFormatImpl(Currencies currencies, Provider<Locale> locale) {
        this.currencies = currencies;
        this.locale = locale;
    }

    @NonNull
    @Override
    public String format(double value) {
        final NumberFormat format = NumberFormat.getCurrencyInstance(locale.get());
        final DecimalFormat decimalFormat = (DecimalFormat) format;
        final DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(currencies.getCurrent().sign());
        decimalFormat.setDecimalFormatSymbols(symbols);
        return format.format(value);
    }

}
