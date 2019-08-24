package com.annasizova.loftcoin.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

public class ChangeFormatImpl implements ChangeFormat {

    private final Locale locale;

    public ChangeFormatImpl(@NonNull Context context) {
        final LocaleListCompat locales = ConfigurationCompat.getLocales(context.getResources().getConfiguration());
        locale = locales.get(0);
    }

    @NonNull
    @Override
    public String format(double value) {
        return String.format(locale, "%.4f%%", value);
    }
}
