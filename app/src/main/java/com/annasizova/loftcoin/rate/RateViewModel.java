package com.annasizova.loftcoin.rate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.data.Currency;
import com.annasizova.loftcoin.db.CoinEntity;

import java.util.List;

import javax.inject.Inject;

public class RateViewModel extends ViewModel{

    private final CoinsRepository repository;
    private final Currencies currencies;
    private final LiveData <List<CoinEntity>> dataSet;
    private final MutableLiveData <Throwable> error = new MutableLiveData<>();
    private final MutableLiveData <Boolean> loading = new MutableLiveData<>();

    @Inject
    RateViewModel(CoinsRepository repository, Currencies currencies) {
        this.repository = repository;
        this.currencies = currencies;
        dataSet = repository.listings();
        refresh();
    }

    void refresh() {
        loading.postValue(true);
        final Currency currency = currencies.getCurrent();
        repository.refresh(currency.code(),
                () -> loading.postValue(false),
                error -> {
            this.error.postValue(error);
            loading.postValue(false);
        });
    }

    void updateCurrency(@NonNull Currency currency) {
        currencies.setCurrent(currency);
        refresh();
    }

    @NonNull
    LiveData<Boolean> loading() {
        return loading;
    }

    @NonNull
    LiveData<List<CoinEntity>> dataSet() {
        return dataSet;
    }

    @NonNull
    LiveData <Throwable> error() {
        return error;
    }
}
