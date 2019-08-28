package com.annasizova.loftcoin.rate;

import androidx.fragment.app.Fragment;

import com.annasizova.loftcoin.util.UtilModule;
import com.annasizova.loftcoin.vm.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = {
        RateModule.class, ViewModelModule.class, UtilModule.class
})
interface RateComponent {

    void inject(RateFragment rateFragment);

    void inject(CurrencyDialog currencyDialog);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder fragment(Fragment fragment);

        RateComponent build();
    }
}
