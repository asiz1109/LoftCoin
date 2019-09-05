package com.annasizova.loftcoin.wallets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.main.MainViewModel;
import com.annasizova.loftcoin.util.PagerDecoration;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class WalletsFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject ViewModelProvider.Factory vmFactory;
    @Inject WalletsAdapter walletsAdapter;
    @Inject TransactionsAdapter transactionsAdapter;
    private MainViewModel mainViewModel;
    private WalletsViewModel walletsViewModel;
    private RecyclerView wallets;
    private RecyclerView transactions;
    private SnapHelper walletsSnapHelper;
    private RecyclerView.OnScrollListener onWalletsScroll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWalletsComponent.builder()
                .fragment(this)
                .build()
                .inject(this);

        mainViewModel = ViewModelProviders
                .of(requireActivity(), vmFactory)
                .get(MainViewModel.class);

        walletsViewModel = ViewModelProviders
                .of(this, vmFactory)
                .get(WalletsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.submitTitle(getString(R.string.wallets));

        wallets = view.findViewById(R.id.wallets);
        wallets.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        wallets.addItemDecoration(new PagerDecoration(view.getContext(), 16));

        walletsSnapHelper = new PagerSnapHelper();
        walletsSnapHelper.attachToRecyclerView(wallets);
        wallets.swapAdapter(walletsAdapter, false);

        final View walletsCard = view.findViewById(R.id.wallet_image);
        disposable.add(walletsViewModel.wallets().subscribe(wallets -> {
            walletsCard.setVisibility(wallets.isEmpty() ? View.VISIBLE : View.GONE);
           walletsAdapter.submitList(wallets);
        }));

        onWalletsScroll = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    final View snapView = walletsSnapHelper.findSnapView(recyclerView.getLayoutManager());
                    if (snapView != null) {
                        walletsViewModel.submitWalletId(recyclerView.getChildItemId(snapView));
                    }
                }
            }
        };

        wallets.addOnScrollListener(onWalletsScroll);

        transactions = view.findViewById(R.id.transactions);
        transactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        transactions.setHasFixedSize(true);
        transactions.swapAdapter(transactionsAdapter, false);

        disposable.add(walletsViewModel.transactions()
                .subscribe(transactionsAdapter::submitList));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_wallet, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add_wallet == item.getItemId()) {
            disposable.add(walletsViewModel
                    .createNextWallet()
                    .subscribe(() -> Toast.makeText(requireContext(), R.string.wallet_created, Toast.LENGTH_SHORT).show(),
                            e -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        walletsSnapHelper.attachToRecyclerView(null);
        wallets.removeOnScrollListener(onWalletsScroll);
        wallets.swapAdapter(null, false);
        transactions.swapAdapter(null, false);
        super.onDestroyView();
    }
}
