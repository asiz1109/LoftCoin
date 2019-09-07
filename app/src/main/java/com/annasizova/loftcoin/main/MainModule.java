package com.annasizova.loftcoin.main;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.AppComponent;
import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.converter.ConverterFragment;
import com.annasizova.loftcoin.fcm.FcmChannel;
import com.annasizova.loftcoin.rate.RateFragment;
import com.annasizova.loftcoin.util.Supplier;
import com.annasizova.loftcoin.wallets.WalletsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface MainModule {

    @Provides
    @Reusable
    static SparseArrayCompat<Supplier<Fragment>> fragments() {
        final SparseArrayCompat<Supplier<Fragment>> fragments = new SparseArrayCompat<>();
        fragments.put(R.id.menu_wallets, WalletsFragment::new);
        fragments.put(R.id.menu_rate, RateFragment::new);
        fragments.put(R.id.menu_converter, ConverterFragment::new);
        return fragments;
    }

    @Provides
    static FcmChannel fcmChannel(FragmentActivity activity) {
        return AppComponent.from(activity.getApplicationContext()).fcmChannel();
    }

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel impl);
}
