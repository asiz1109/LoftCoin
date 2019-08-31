package com.annasizova.loftcoin;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.core.os.ConfigurationCompat;
import androidx.room.Room;

import com.annasizova.loftcoin.db.LoftDB;

import java.util.Locale;

import javax.inject.Singleton;

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

    @Provides
    @Singleton
    static LoftDB loftDb(Context context) {
        return Room.databaseBuilder(context, LoftDB.class, "loft").build();
    }
}
