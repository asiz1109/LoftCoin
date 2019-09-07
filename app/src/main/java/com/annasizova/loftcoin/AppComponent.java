package com.annasizova.loftcoin;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.data.DataModule;
import com.annasizova.loftcoin.data.WalletsRepository;
import com.annasizova.loftcoin.fcm.FcmChannel;
import com.annasizova.loftcoin.fcm.FcmModule;
import com.annasizova.loftcoin.fcm.FcmService;
import com.annasizova.loftcoin.rx.RxModule;
import com.annasizova.loftcoin.rx.RxSchedulers;

import java.util.Locale;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component (modules = {AppModule.class, DataModule.class, RxModule.class, FcmModule.class})
public interface AppComponent {

    @NonNull
    static AppComponent from(@NonNull Context context) {
        final Context appContext = context.getApplicationContext();
        if (appContext instanceof LoftApp) {
            return ((LoftApp) appContext).getAppComponent();
        }
        throw new IllegalArgumentException("ApplicationContext should be an instance of LoftApp");
    }

    void inject(FcmService service);

    Provider<Locale> locale();

    CoinsRepository coinsRepository();

    WalletsRepository walletsRepository();

    Currencies currencies();

    RxSchedulers schedulers();

    FcmChannel fcmChannel();

    @Component.Factory
    interface Factory {

        AppComponent create(@BindsInstance Application app);
    }
}
