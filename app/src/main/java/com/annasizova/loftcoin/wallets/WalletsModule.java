package com.annasizova.loftcoin.wallets;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.AppComponent;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.data.WalletsRepository;
import com.annasizova.loftcoin.main.MainViewModel;
import com.annasizova.loftcoin.rx.RxSchedulers;

import java.util.Locale;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface WalletsModule {

    @Provides
    @Reusable
    static AppComponent appComponent(Fragment fmt) {
        return AppComponent.from(fmt.requireContext());
    }

    @Provides
    static WalletsRepository walletsRepository(AppComponent appComponent) {
        return appComponent.walletsRepository();
    }

    @Provides
    static Currencies currencies(AppComponent appComponent) {
        return appComponent.currencies();
    }

    @Provides
    static Locale locale(AppComponent appComponent) {
        return appComponent.locale().get();
    }

    @Provides
    static RxSchedulers schedulers(AppComponent appComponent) {
        return appComponent.schedulers();
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
