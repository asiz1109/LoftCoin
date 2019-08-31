package com.annasizova.loftcoin.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CurrenciesImpl implements Currencies {

    private static final String KEY_CODE = "code";
    private final Context context;

    @Inject
    CurrenciesImpl(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public List<Currency> getAvailableCurrencies() {
        return Arrays.asList(Currency.values());
    }

    @NonNull
    @Override
    public Currency getCurrent() {
        return Currency.valueOf(getPrefs().getString(KEY_CODE, Currency.USD.code()));
    }

    @Override
    public void setCurrent(@NonNull Currency currency) {
        getPrefs().edit().putString(KEY_CODE, currency.code()).apply();
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences("currencies", Context.MODE_PRIVATE);
    }

}
