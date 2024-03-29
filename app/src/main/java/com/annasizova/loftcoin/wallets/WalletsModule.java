package com.annasizova.loftcoin.wallets;

import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.AppComponent;
import com.annasizova.loftcoin.data.WalletsRepository;
import com.annasizova.loftcoin.main.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface WalletsModule {

    @Provides
    static WalletsRepository walletsRepository(AppComponent appComponent) {
        return appComponent.walletsRepository();
    }

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel impl);

    @Binds
    @IntoMap
    @ClassKey(WalletsViewModel.class)
    ViewModel ratesViewModel(WalletsViewModel impl);
}
