package com.annasizova.loftcoin.rate;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.annasizova.loftcoin.data.Coin;
import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.data.Quote;
import com.annasizova.loftcoin.util.ChangeFormat;
import com.annasizova.loftcoin.util.ChangeFormatImpl;
import com.annasizova.loftcoin.util.ImgUrlFormat;
import com.annasizova.loftcoin.util.ImgUrlFormatImpl;
import com.annasizova.loftcoin.util.PriceFormat;
import com.annasizova.loftcoin.util.PriceFormatImpl;

import java.util.ArrayList;
import java.util.List;

public class RateViewModel extends ViewModel{

    private final CoinsRepository repository;
    private final PriceFormat priceFormat;
    private final ChangeFormat changeFormat;
    private final ImgUrlFormat imgUrlFormat;
    private final MutableLiveData <List<CoinRate>> dataSet = new MutableLiveData<>();
    private final MutableLiveData <Throwable> error = new MutableLiveData<>();
    private final MutableLiveData <Boolean> loading = new MutableLiveData<>();

    private RateViewModel(@NonNull CoinsRepository repository,
                          @NonNull PriceFormat priceFormat,
                          @NonNull ChangeFormat changeFormat,
                          @NonNull ImgUrlFormat imgUrlFormat) {
        this.repository = repository;
        this.priceFormat = priceFormat;
        this.changeFormat = changeFormat;
        this.imgUrlFormat = imgUrlFormat;
        refresh();
    }

    void refresh() {
        loading.postValue(true);
        repository.listings("USD", coins -> {
            final List<CoinRate> rates = new ArrayList<>(coins.size());
            for (Coin coin : coins) {
                final CoinRate.Builder builder = CoinRate.builder()
                        .id(coin.getId())
                        .symbol(coin.getSymbol())
                        .imageUrl(imgUrlFormat.format(coin.getId()));
                final Quote quote = coin.getQuotes().get("USD");
                if (quote != null) {
                    builder.price(priceFormat.format(quote.getPrice()));
                    builder.change24h(changeFormat.format(quote.getChange24h()));
                    builder.isChange24hNegative(quote.getChange24h() < 0d);
                }
                rates.add(builder.build());
            }
            dataSet.postValue(rates);
            loading.postValue(false);
        }, error -> {
            this.error.postValue(error);
            loading.postValue(false);
        });
    }

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

    static class Factory implements ViewModelProvider.Factory {

        private Context context;

        Factory (@NonNull Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RateViewModel(
                    CoinsRepository.get(),
                    new PriceFormatImpl(context),
                    new ChangeFormatImpl(context),
                    new ImgUrlFormatImpl()
            );
        }
    }
}
