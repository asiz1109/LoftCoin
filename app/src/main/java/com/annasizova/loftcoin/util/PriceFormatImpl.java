package com.annasizova.loftcoin.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatImpl implements PriceFormat {

    private final Locale locale;

    public PriceFormatImpl(@NonNull Context context) {
        final LocaleListCompat locales = ConfigurationCompat.getLocales(context.getResources().getConfiguration());
        locale = locales.get(0);
    }

    @NonNull
    @Override
    public String format(double value) {
        return NumberFormat.getCurrencyInstance(locale).format(value);
    }

}
