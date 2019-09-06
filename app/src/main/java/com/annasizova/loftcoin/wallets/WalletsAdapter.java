package com.annasizova.loftcoin.wallets;

import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.adapter.StableIdDiff;
import com.annasizova.loftcoin.db.Wallet;
import com.annasizova.loftcoin.util.ImgUrlFormat;
import com.annasizova.loftcoin.util.PriceFormat;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

class WalletsAdapter extends ListAdapter<Wallet, WalletsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ImgUrlFormat urlFormat;
    private PriceFormat priceFormat;

    @Inject
    WalletsAdapter(ImgUrlFormat urlFormat, PriceFormat priceFormat) {
        super(new StableIdDiff<>());
        this.urlFormat = urlFormat;
        this.priceFormat = priceFormat;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).coin().id();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.li_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Wallet wallet = Objects.requireNonNull(getItem(position));

        Picasso.get().load(urlFormat.format(wallet.coin().id())).into(holder.logo);

        holder.symbol.setText(wallet.coin().symbol());
        holder.balance1.setText(priceFormat.format(wallet.balance1(), wallet.coin().symbol()));
        holder.balance2.setText(priceFormat.format(wallet.balance2()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView logo;
        final TextView symbol;
        final TextView balance1;
        final TextView balance2;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            symbol = itemView.findViewById(R.id.symbol);
            balance1 = itemView.findViewById(R.id.balance1);
            balance2 = itemView.findViewById(R.id.balance2);
            logo.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(),
                            view.getWidth() / 2);
                }
            });
            logo.setClipToOutline(true);
        }
    }
}
