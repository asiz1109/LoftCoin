package com.annasizova.loftcoin.util;

import dagger.Binds;
import dagger.Module;

@Module
public interface UtilModule {

    @Binds
    ImgUrlFormat imgUrlFormat(ImgUrlFormatImpl impl);

    @Binds
    PriceFormat priceFormat(PriceFormatImpl impl);

    @Binds
    ChangeFormat changeFormat(ChangeFormatImpl impl);

}
