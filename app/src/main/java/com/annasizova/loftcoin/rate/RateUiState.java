package com.annasizova.loftcoin.rate;

import androidx.annotation.Nullable;

import com.annasizova.loftcoin.db.CoinEntity;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;

@AutoValue
abstract class RateUiState {

    abstract List<CoinEntity> rates();

    @Nullable
    abstract String error();

    abstract boolean isRefreshing();

    static RateUiState loading() {
        return new AutoValue_RateUiState(Collections.emptyList(), null, true);
    }

    static RateUiState success(List<CoinEntity> rates) {
        return new AutoValue_RateUiState(rates, null, false);
    }

    static RateUiState failure(Throwable e) {
        return new AutoValue_RateUiState(Collections.emptyList(), e.getMessage(), false);
    }
}
