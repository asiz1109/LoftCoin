package com.annasizova.loftcoin.util;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.data.Currencies;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Reusable;

@Reusable
public class PriceFormatImpl implements PriceFormat {

    private final Currencies currencies;
    private final Provider<Locale> locale;

    @Inject
    PriceFormatImpl(Currencies currencies, Provider<Locale> locale) {
        this.currencies = currencies;
        this.locale = locale;
    }

    @NonNull
    @Override
    public String format(double value) {
        return format(value, currencies.getCurrent().sign());
    }

    @Override
    public String format(double value, String sign) {
        final NumberFormat format = NumberFormat.getCurrencyInstance(locale.get());
        format.setMaximumFractionDigits(5);
        final DecimalFormat decimalFormat = (DecimalFormat) format;
        final DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(sign);
        decimalFormat.setDecimalFormatSymbols(symbols);
        return format.format(value).replace('\u00A0', ' ').trim();
    }
}
