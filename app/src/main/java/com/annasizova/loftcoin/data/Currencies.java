package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;

public interface Currencies {

    @NonNull
    List<Currency> getAvailableCurrencies();

    @NonNull
    Currency getCurrent();

    void setCurrent(@NonNull Currency currency);

    @Nullable
    Observable<Currency> current();


}
