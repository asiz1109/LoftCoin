package com.annasizova.loftcoin.vm;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface ViewModelModule {

    @Binds
    @Reusable
    ViewModelProvider.Factory viewModelFactory(ViewModelFactory impl);
}
