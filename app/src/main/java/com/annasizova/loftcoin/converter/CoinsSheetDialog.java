package com.annasizova.loftcoin.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.rx.RxRecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class CoinsSheetDialog extends BottomSheetDialogFragment {

    private static final String KEY_MODE = "mode";
    private static final int MODE_FROM = 1;
    private static final int MODE_TO = 2;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject ViewModelProvider.Factory vmFactory;
    @Inject CoinsSheetAdapter adapter;
    private ConverterViewModel viewModel;
    private RecyclerView recyclerView;
    private int mode = MODE_FROM;

    static void chooseFromCoin(@NonNull FragmentManager fm) {
        final CoinsSheetDialog dialog = new CoinsSheetDialog();
        final Bundle arguments = new Bundle();
        arguments.putInt(KEY_MODE, MODE_FROM);
        dialog.setArguments(arguments);
        dialog.show(fm, CoinsSheetDialog.class.getSimpleName());
    }

    static void chooseToCoin(@NonNull FragmentManager fm) {
        final CoinsSheetDialog dialog = new CoinsSheetDialog();
        final Bundle arguments = new Bundle();
        arguments.putInt(KEY_MODE, MODE_TO);
        dialog.setArguments(arguments);
        dialog.show(fm, CoinsSheetDialog.class.getSimpleName());
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_BottomSheet_Dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerConverterComponent.builder()
                .fragment(requireParentFragment())
                .build()
                .inject(this);

        viewModel = ViewModelProviders
                .of(requireParentFragment(), vmFactory)
                .get(ConverterViewModel.class);

        mode = requireArguments().getInt(KEY_MODE, MODE_FROM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.currency_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.currency_dialog_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.swapAdapter(adapter, false);

        disposable.add(viewModel.topCoins().subscribe(adapter::submitList));
        disposable.add(RxRecyclerView.onItemClick(recyclerView)
                .map(recyclerView::getChildAdapterPosition)
                .doOnNext(position -> {
                    if (MODE_FROM == mode) {
                        viewModel.changeFromCoin(position);
                    } else {
                        viewModel.changeToCoin(position);
                    }
                })
                .doOnNext(position -> dismissAllowingStateLoss())
                .subscribe());
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        recyclerView.swapAdapter(adapter, false);
        super.onDestroyView();
    }
}
