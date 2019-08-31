package com.annasizova.loftcoin.data;

import androidx.annotation.NonNull;
import java.util.List;

public interface Currencies {

    @NonNull
    List<Currency> getAvailableCurrencies();

    @NonNull
    Currency getCurrent();

    void setCurrent(@NonNull Currency currency);

}
