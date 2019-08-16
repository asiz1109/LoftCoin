package com.annasizova.loftcoin.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.R;

public class MainViewModel extends ViewModel {

    private final MutableLiveData <String> selectedTitle = new MutableLiveData<>();
    private final MutableLiveData <Integer> selectedId = new MutableLiveData<>();

    public MainViewModel() {
        selectedId.postValue(R.id.menu_wallets);
    }

    public void submitTitle(String title) {
        selectedTitle.postValue(title);
    }

    public void submitSelectedId(int id) {
        selectedId.postValue(id);
    }

    @NonNull
    public LiveData <String> selectedTitle() {
        return selectedTitle;
    }

    @NonNull
    public LiveData <Integer> selectedId() {
        return selectedId;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
