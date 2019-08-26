package com.annasizova.loftcoin.rate;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.annasizova.loftcoin.R;

import javax.inject.Inject;

public class CurrencyDialog extends DialogFragment {

    static final String TAG = "CurrencyDialog";
    @Inject ViewModelProvider.Factory vmFactory;
    private RateViewModel rateViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Fragment parentFragment = requireParentFragment();
        DaggerRateComponent.builder().fragment(parentFragment).build().inject(this);
        rateViewModel = ViewModelProviders.of(parentFragment,vmFactory).get(RateViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AppCompatDialog dialog = new AppCompatDialog(requireContext());
        dialog.setTitle(R.string.choose_currency);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.currency_dialog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
