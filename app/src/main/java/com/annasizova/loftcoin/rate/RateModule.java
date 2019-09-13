package com.annasizova.loftcoin.rate;

import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.AppComponent;
import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.main.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface RateModule {

    @Provides
    static CoinsRepository coinRepository(AppComponent appComponent) {
        return  appComponent.coinsRepository();
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
