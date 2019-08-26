package com.annasizova.loftcoin;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.core.os.ConfigurationCompat;

import java.util.Locale;

import dagger.Module;
import dagger.Provides;

@Module
interface AppModule {

    @Provides
    static Context context(Application app) {
        return app.getApplicationContext();
    }

    @Provides
    static Locale locale(Context context) {
        final Configuration configuration = context.getResources().getConfiguration();
        return ConfigurationCompat.getLocales(configuration).get(0);
    }
}
