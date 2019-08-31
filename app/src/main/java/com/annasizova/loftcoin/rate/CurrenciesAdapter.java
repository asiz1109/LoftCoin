package com.annasizova.loftcoin.rate;

import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.data.Currencies;
import com.annasizova.loftcoin.data.Currency;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>  {

    private final List<Currency> currencies;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;

    @Inject
    CurrenciesAdapter(Currencies currencies){
        this.currencies = currencies.getAvailableCurrencies();
    }

    void setOnItemClick(@Nullable OnItemClick listener) {
        onItemClick = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.li_currency, parent, false));
        viewHolder.symbol.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth()/2);
            }
        });
        viewHolder.symbol.setClipToOutline(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Currency currency = currencies.get(position);
        holder.symbol.setText(currency.sign());
        holder.name.setText(String.format(Locale.US, "%s | %s", currency.code(), holder.itemView.getContext().getString(currency.nameResId())));
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) {
                onItemClick.onItemClick(currency, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView symbol, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol = itemView.findViewById(R.id.symbol);
            name = itemView.findViewById(R.id.name);
        }
    }

    interface OnItemClick {

        void onItemClick(Currency currency, int position);
    }
}
