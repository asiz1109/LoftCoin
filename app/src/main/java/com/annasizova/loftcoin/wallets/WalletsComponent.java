package com.annasizova.loftcoin.wallets;

import androidx.fragment.app.Fragment;

import com.annasizova.loftcoin.common.FragmentModule;
import com.annasizova.loftcoin.util.UtilModule;
import com.annasizova.loftcoin.vm.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = {FragmentModule.class, WalletsModule.class, ViewModelModule.class, UtilModule.class})
interface WalletsComponent {

    void inject(WalletsFragment fmt);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder fragment(Fragment fmt);

        WalletsComponent build();

    }
}
