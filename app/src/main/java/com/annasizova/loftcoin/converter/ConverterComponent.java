package com.annasizova.loftcoin.converter;

import androidx.fragment.app.Fragment;

import com.annasizova.loftcoin.common.FragmentModule;
import com.annasizova.loftcoin.util.UtilModule;
import com.annasizova.loftcoin.vm.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {FragmentModule.class, ConverterModule.class, ViewModelModule.class, UtilModule.class})
interface ConverterComponent {

    void inject(ConverterFragment fmt);

    void inject(CoinsSheetDialog fmt);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder fragment(Fragment fmt);

        ConverterComponent build();

    }
}
