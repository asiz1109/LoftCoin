package com.annasizova.loftcoin.rate;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.data.Coin;
import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.util.Function;

import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class RateViewModel extends ViewModel{

    private final CoinsRepository repository;
    private final Function<List<Coin>, List<CoinRate>> rateMapper;
    private final Currencies currencies;
    private final MutableLiveData <List<CoinRate>> dataSet = new MutableLiveData<>();
    private final MutableLiveData <Throwable> error = new MutableLiveData<>();
    private final MutableLiveData <Boolean> loading = new MutableLiveData<>();

    @Inject
    RateViewModel(CoinsRepository repository, Function<List<Coin>, List<CoinRate>> rateMapper, Currencies currencies) {
        this.repository = repository;
        this.rateMapper = rateMapper;
        this.currencies = currencies;
        refresh();
    }

    void refresh() {
        loading.postValue(true);
        final Pair<Currency, Locale> pair = currencies.getCurrent();
        repository.listings(Objects.requireNonNull(pair.first).getCurrencyCode(), coins -> {
            dataSet.postValue(rateMapper.apply(coins));
            loading.postValue(false);
        }, error -> {
            this.error.postValue(error);
            loading.postValue(false);
        });
    }

//    void updateCurrency(Pair<Currency, Locale> pair) {
//        currencies.setCurrent();
//        refresh();
//    }

    @NonNull
    LiveData<Boolean> loading() {
        return loading;
    }

    @NonNull
    LiveData<List<CoinRate>> dataSet() {
        return dataSet;
    }

    @NonNull
    LiveData <Throwable> error() {
        return error;
    }
}
