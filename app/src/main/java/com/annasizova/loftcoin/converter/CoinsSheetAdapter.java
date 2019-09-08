package com.annasizova.loftcoin.converter;

import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.adapter.StableIdDiff;
import com.annasizova.loftcoin.db.CoinEntity;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

class CoinsSheetAdapter extends ListAdapter<CoinEntity, CoinsSheetAdapter.ViewHolder> {

    private LayoutInflater inflater;

    @Inject
    CoinsSheetAdapter() {
        super(new StableIdDiff<>());
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).id();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.li_currency, parent, false));
        holder.symbol.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 2f);
            }
        });
        holder.symbol.setClipToOutline(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CoinEntity coin = Objects.requireNonNull(getItem(position));
        holder.symbol.setText(String.valueOf(coin.symbol().charAt(0)));
        holder.name.setText(String.format(Locale.US, "%s | %s", coin.name(), coin.symbol()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView symbol;

        private final TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol = itemView.findViewById(R.id.symbol);
            name = itemView.findViewById(R.id.name);
        }

    }
}
