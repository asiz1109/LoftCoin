package com.annasizova.loftcoin.fcm;

import dagger.Binds;
import dagger.Module;

@Module
public interface FcmModule {

    @Binds
    FcmChannel fcmChannel(FcmChannelImpl impl);

}
