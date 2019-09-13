package com.annasizova.loftcoin.converter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.rx.RxSchedulers;
import com.annasizova.loftcoin.util.PriceFormat;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class ConverterViewModel extends ViewModel {

    private final Subject<Integer> from = BehaviorSubject.createDefault(0);
    private final Subject<Integer> to = BehaviorSubject.createDefault(1);
    private final Subject<String> fromValue = BehaviorSubject.create();
    private final Subject<String> toValue = BehaviorSubject.create();
    private final Observable<List<CoinEntity>> topCoins;
    private final Observable<CoinEntity> fromCoin;
    private final Observable<CoinEntity> toCoin;
    private final Observable<Double> factor;
    private final RxSchedulers schedulers;
    private final PriceFormat priceFormat;

    @Inject
    ConverterViewModel(CoinsRepository coinsRepo, RxSchedulers schedulers, PriceFormat priceFormat) {
        this.priceFormat = priceFormat;
        this.schedulers = schedulers;
        topCoins = coinsRepo.top(5).replay(1).autoConnect().subscribeOn(schedulers.io());
        fromCoin = topCoins.switchMap(coins -> from.map(coins::get))
                .replay(1)
                .autoConnect()
                .subscribeOn(schedulers.io());
        toCoin = topCoins.switchMap(coins -> to.map(coins::get))
                .replay(1)
                .autoConnect()
                .subscribeOn(schedulers.io());
        factor = fromCoin.switchMap(f -> toCoin.map(t -> f.price() / t.price()))
                .replay(1)
                .autoConnect()
                .subscribeOn(schedulers.io());
    }

    @NonNull
    Observable<List<CoinEntity>> topCoins() {
        return topCoins.observeOn(schedulers.main());
    }

    @NonNull
    Observable<CoinEntity> fromCoin() {
        return fromCoin.observeOn(schedulers.main());
    }

    @NonNull
    Observable<CoinEntity> toCoin() {
        return toCoin.observeOn(schedulers.main());
    }

    @NonNull
    Observable<String> toValue() {
        return fromValue.compose(parseValue())
                .switchMap(value -> factor.map(factor -> value * factor))
                .compose(formatValue())
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main());
    }

    @NonNull
    Observable<String> fromValue() {
        return toValue.compose(parseValue())
                .switchMap(value -> factor.map(factor -> value / factor))
                .compose(formatValue())
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main());
    }

    void changeFromCoin(int position) {
        from.onNext(position);
    }

    void changeToCoin(int position) {
        to.onNext(position);
    }

    void changeFromValue(String text) {
        fromValue.onNext(checkText(text));
    }

    void changeToValue(String text) {
        toValue.onNext(checkText(text));
    }

    @NonNull
    private ObservableTransformer<String, Double> parseValue() {
        return upstream -> upstream.distinctUntilChanged()
                .map(value -> value.isEmpty() ? "0" : value)
                .map(value -> value.trim().replace(',', '.'))
                .map(value -> value.replaceAll("\\s+", ""))
                .map(Double::parseDouble);
    }

    @NonNull
    private ObservableTransformer<Double, String> formatValue() {
        return upstream -> upstream.map(value -> {
            if (value > 0) {
                return priceFormat.format(value, "");
            } else {
                return "";
            }
        });
    }

    String checkText (String text) {
        if (text.startsWith(".")) {
            return "0";
        }
        text = text.replace("\u00A0", "");
        String newText = text;
        int countDot = newText.length() - newText.replace(",", "").replace(".", "").length();
        if (countDot > 1) {
            return "0";
        }
        return text;
    }
}
