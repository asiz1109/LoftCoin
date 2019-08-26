package com.annasizova.loftcoin.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<?>, Provider<ViewModel>> providers;

    @Inject
    ViewModelFactory (Map<Class<?>, Provider<ViewModel>> providers) {
        this.providers = providers;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        final Provider<ViewModel> provider = providers.get(modelClass);
        if (provider != null) {
            return (T) provider.get();
        }
        for (final Map.Entry<Class<?>, Provider<ViewModel>> entry : providers.entrySet()) {
            if (modelClass.isAssignableFrom(entry.getKey())) {
                return (T) entry.getValue().get();
            }
        }
        throw new IllegalArgumentException("No such provider for " + modelClass);
    }
}
