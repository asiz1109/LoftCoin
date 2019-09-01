package com.annasizova.loftcoin.rate;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.AppComponent;
import com.annasizova.loftcoin.data.Coin;
import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.main.MainViewModel;
import com.annasizova.loftcoin.util.Function;

import java.util.List;
import java.util.Locale;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface RateModule {

    @Provides
    @Reusable
    static AppComponent appComponent(Fragment fragment) {
        return AppComponent.from(fragment.requireContext());
    }

    @Provides
    static CoinsRepository coinRepository(AppComponent appComponent) {
        return  appComponent.coinsRepository();
    }

    @Provides
    static Currencies currencies(AppComponent appComponent) {
        return  appComponent.currencies();
    }

    @Provides
    static Locale locale(AppComponent appComponent) {
        return appComponent.locale().get();
    }

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel impl);

    @Binds
    @IntoMap
    @ClassKey(RateViewModel.class)
    ViewModel rateViewModel(RateViewModel impl);
}
