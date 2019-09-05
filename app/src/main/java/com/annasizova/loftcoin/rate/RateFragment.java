package com.annasizova.loftcoin.rate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.main.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.disposables.CompositeDisposable;

public class RateFragment extends Fragment {

    @Inject ViewModelProvider.Factory vmFactory;
    @Inject RateAdapter rateAdapter;
    private RecyclerView recyclerView;
    private RateViewModel rateViewModel;
    private MainViewModel mainViewModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerRateComponent.builder().fragment(this).build().inject(this);
        rateViewModel = ViewModelProviders.of(this, vmFactory).get(RateViewModel.class);
        mainViewModel = ViewModelProviders.of(requireActivity(), vmFactory).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel.submitTitle(getString(R.string.rate));

        recyclerView = view.findViewById(R.id.rate_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.swapAdapter(rateAdapter, false);
        recyclerView.setHasFixedSize(true);

        final SwipeRefreshLayout refreshLayout = view.findViewById(R.id.rate_refresh);
        refreshLayout.setOnRefreshListener(rateViewModel::refresh);

        disposable.add(rateViewModel.uiState().subscribe(state -> {
            refreshLayout.setRefreshing(state.isRefreshing());
            if(!state.rates().isEmpty()) {
                rateAdapter.submitList(state.rates());
            }
            final String errorMessage = state.error();
            if(errorMessage != null) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.rate_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_change_currency) {
                final CurrencyDialog currencyDialog = new CurrencyDialog();
                currencyDialog.show(getChildFragmentManager(), CurrencyDialog.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.swapAdapter(null, false);
        disposable.clear();
    }
}
