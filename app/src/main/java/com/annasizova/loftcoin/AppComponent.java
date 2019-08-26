package com.annasizova.loftcoin;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.data.CoinsRepository;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.data.DataModule;

import java.util.Locale;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component (modules = {
    AppModule.class, DataModule.class
})
public interface AppComponent {

    @NonNull
    static AppComponent from(@NonNull Context context) {
        final Context appContext = context.getApplicationContext();
        if (appContext instanceof LoftApp) {
            return ((LoftApp) appContext).getAppComponent();
        }
        throw new IllegalArgumentException("ApplicationContext should be an instance of LoftApp");
    }

    Provider<Locale> locale();

    CoinsRepository coinsRepository();

    Currencies currencies();

    @Component.Factory
    interface Factory {

        AppComponent create(@BindsInstance Application app);
    }
}
