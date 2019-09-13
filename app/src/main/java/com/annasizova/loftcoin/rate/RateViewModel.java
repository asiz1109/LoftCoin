package com.annasizova.loftcoin.rate;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.data.Currency;
import com.annasizova.loftcoin.rx.RxSchedulers;

import javax.inject.Inject;

import dagger.Reusable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

@Reusable
public class RateViewModel extends ViewModel {

    private final CoinsRepository repository;
    private final Currencies currencies;
    private final RxSchedulers schedulers;
    private final Subject<Boolean> sourceOfTrue;
    private final Observable<RateUiState> uiState;

    @Inject
    RateViewModel(CoinsRepository repository, Currencies currencies, RxSchedulers schedulers) {
        this.repository = repository;
        this.currencies = currencies;
        this.schedulers = schedulers;
        sourceOfTrue = BehaviorSubject.createDefault(true);
        uiState = sourceOfTrue
                .observeOn(schedulers.io())
                .flatMap(refresh -> currencies.current())
                .map(Currency::code)
                .flatMap(currencyCode -> repository.listings(currencyCode).map(RateUiState::success).onErrorReturn(RateUiState::failure).startWith(RateUiState.loading()))
                .subscribeOn(schedulers.io());
    }

    void refresh() {
        sourceOfTrue.onNext(true);
    }

    @NonNull
    Observable<RateUiState> uiState() {
        return uiState.observeOn(schedulers.main());
    }

}
