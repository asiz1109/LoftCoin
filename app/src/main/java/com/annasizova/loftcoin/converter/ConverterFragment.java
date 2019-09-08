package com.annasizova.loftcoin.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.main.MainViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ConverterFragment extends Fragment {

    private MainViewModel mainViewModel;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject ViewModelProvider.Factory vmFactory;
    private ConverterViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerConverterComponent.builder().fragment(this).build().inject(this);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(this, vmFactory).get(ConverterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.submitTitle(getString(R.string.converter));

        final TextView fromCoin = view.findViewById(R.id.from_coin);
        final TextView toCoin = view.findViewById(R.id.to_coin);

        disposable.add(viewModel.fromCoin().subscribe(coin -> fromCoin.setText(coin.symbol())));
        disposable.add(viewModel.toCoin().subscribe(coin -> toCoin.setText(coin.symbol())));
        disposable.add(RxView.clicks(fromCoin).subscribe(none -> CoinsSheetDialog.chooseFromCoin(getChildFragmentManager())));
        disposable.add(RxView.clicks(toCoin).subscribe(none -> CoinsSheetDialog.chooseToCoin(getChildFragmentManager())));

        final EditText from = view.findViewById(R.id.from);
        final EditText to = view.findViewById(R.id.to);

        disposable.add(RxTextView.textChanges(from).map(CharSequence::toString).subscribe(viewModel::changeFromValue));
        disposable.add(RxTextView.textChanges(to).map(CharSequence::toString).subscribe(viewModel::changeToValue));
        disposable.add(viewModel.fromValue().filter(value -> !from.hasFocus()).subscribe(from::setText));
        disposable.add(viewModel.toValue().filter(value -> !to.hasFocus()).subscribe(to::setText));
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        super.onDestroyView();
    }
}
