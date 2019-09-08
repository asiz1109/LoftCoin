package com.annasizova.loftcoin.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
class CurrenciesImpl implements Currencies {

    private static final String KEY_CODE = "code";
    private final SharedPreferences prefs;

    @Inject
    CurrenciesImpl(Context context) {
        prefs = context.getSharedPreferences("currencies", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public List<Currency> getAvailableCurrencies() {
        return Arrays.asList(Currency.values());
    }

    @NonNull
    @Override
    public Currency getCurrent() {
        return Currency.valueOf(prefs.getString(KEY_CODE, Currency.USD.code()));
    }

    @Override
    public void setCurrent(@NonNull Currency currency) {
        prefs.edit().putString(KEY_CODE, currency.code()).apply();
    }

    @Nullable
    @Override
    public Observable<Currency> current() {
        return Observable.create(emitter -> {
            final SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> emitter.onNext(getCurrent());
            emitter.setCancellable(() -> prefs.unregisterOnSharedPreferenceChangeListener(listener));
            prefs.registerOnSharedPreferenceChangeListener(listener);
            emitter.onNext(getCurrent());
        });
    }
}
