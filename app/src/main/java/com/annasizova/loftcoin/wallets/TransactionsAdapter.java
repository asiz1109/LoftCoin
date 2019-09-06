package com.annasizova.loftcoin.wallets;

import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.adapter.StableIdDiff;
import com.annasizova.loftcoin.db.Transaction;
import com.annasizova.loftcoin.util.PriceFormat;

import java.util.Objects;

import javax.inject.Inject;

class TransactionsAdapter extends ListAdapter<Transaction.View, TransactionsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private PriceFormat priceFormat;

    @Inject
    TransactionsAdapter(PriceFormat priceFormat) {
        super(new StableIdDiff<>());
        this.priceFormat = priceFormat;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).id();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.li_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction.View transaction = Objects.requireNonNull(getItem(position));

        holder.amount1.setText(priceFormat.format(transaction.amount1(), transaction.symbol()));
        holder.amount2.setText(priceFormat.format(transaction.amount2()));

        final Resources res = holder.itemView.getResources();
        final Resources.Theme theme = holder.itemView.getContext().getTheme();

        final int changeColor;
        if (transaction.amount1() < 0) {
            changeColor = ResourcesCompat.getColor(res, R.color.watermelon, theme);
        } else {
            changeColor = ResourcesCompat.getColor(res, R.color.weird_green, theme);
        }
        holder.amount2.setTextColor(changeColor);

        holder.timestamp.setText(DateUtils.formatDateTime(
                holder.itemView.getContext(), transaction.timestamp(), DateUtils.FORMAT_SHOW_YEAR));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView amount1;
        final TextView amount2;
        final TextView timestamp;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount1 = itemView.findViewById(R.id.amount1);
            amount2 = itemView.findViewById(R.id.amount2);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}
