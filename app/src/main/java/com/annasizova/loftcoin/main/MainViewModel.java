package com.annasizova.loftcoin.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.annasizova.loftcoin.R;

import java.util.Objects;

public class MainViewModel extends ViewModel {

    private final MutableLiveData <String> selectedTitle = new MutableLiveData<>();
    private final MutableLiveData <Integer> selectedId = new MutableLiveData<>();

    public MainViewModel() {
        selectedId.postValue(R.id.menu_rate);
    }

    public void submitTitle(String title) {
        selectedTitle.postValue(title);
    }

    public void submitSelectedId(int id) {
        if (!Objects.equals(id, selectedId.getValue())) {
            selectedId.postValue(id);
        }
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
