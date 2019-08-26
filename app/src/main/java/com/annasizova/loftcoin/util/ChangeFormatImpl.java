package com.annasizova.loftcoin.util;

import androidx.annotation.NonNull;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Reusable;

@Reusable
public class ChangeFormatImpl implements ChangeFormat {

    private final Provider<Locale> locale;

    @Inject
    ChangeFormatImpl(Provider<Locale> locale) {
        this.locale = locale;
    }

    @NonNull
    @Override
    public String format(double value) {
        return String.format(locale.get(), "%.4f%%", value);
    }
}
