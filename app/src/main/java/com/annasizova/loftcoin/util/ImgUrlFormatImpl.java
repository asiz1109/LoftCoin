package com.annasizova.loftcoin.util;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.BuildConfig;

import java.util.Locale;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
public class ImgUrlFormatImpl implements ImgUrlFormat {

    @Inject
    ImgUrlFormatImpl() {
    }

    @NonNull
    @Override
    public String format(long id) {
        return String.format(Locale.US, "%scoins/64x64/%d.png", BuildConfig.CMC_IMG_ENDPOINT, id);
    }
}
