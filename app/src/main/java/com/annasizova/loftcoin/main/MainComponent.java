package com.annasizova.loftcoin.main;

import androidx.fragment.app.FragmentActivity;

import com.annasizova.loftcoin.vm.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = {
        MainModule.class, ViewModelModule.class
})
interface MainComponent {

    void inject(MainActivity activity);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder activity(FragmentActivity activity);

        MainComponent build();
    }
}
