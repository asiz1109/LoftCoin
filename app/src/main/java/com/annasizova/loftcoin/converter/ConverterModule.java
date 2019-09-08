package com.annasizova.loftcoin.converter;

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
interface ConverterModule {

    @Provides
    static CoinsRepository coinsRepository(AppComponent appComponent) {
        return appComponent.coinsRepository();
    }

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel impl);

    @Binds
    @IntoMap
    @ClassKey(ConverterViewModel.class)
    ViewModel converterViewModel(ConverterViewModel impl);
}
