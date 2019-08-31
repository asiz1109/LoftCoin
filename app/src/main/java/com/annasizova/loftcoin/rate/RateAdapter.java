package com.annasizova.loftcoin.rate;

import android.content.res.Resources;
import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.db.CoinEntity;
import com.annasizova.loftcoin.util.ChangeFormat;
import com.annasizova.loftcoin.util.ImgUrlFormat;
import com.annasizova.loftcoin.util.PriceFormat;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RateAdapter extends ListAdapter <CoinEntity, RateAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private final PriceFormat priceFormat;
    private final ChangeFormat changeFormat;
    private final ImgUrlFormat imgUrlFormat;

    @Inject
    RateAdapter(PriceFormat priceFormat, ChangeFormat changeFormat, ImgUrlFormat imgUrlFormat) {
        super(new DiffUtil.ItemCallback<CoinEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull CoinEntity oldItem, @NonNull CoinEntity newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CoinEntity oldItem, @NonNull CoinEntity newItem) {
                return Objects.equals(oldItem, newItem);
            }

            @Nullable
            @Override
            public Object getChangePayload(@NonNull CoinEntity oldItem, @NonNull CoinEntity newItem) {
                return newItem;
            }
        });
        this.priceFormat = priceFormat;
        this.changeFormat = changeFormat;
        this.imgUrlFormat = imgUrlFormat;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_rate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CoinEntity coin = getItem(position);
        Picasso.get().load(imgUrlFormat.format(coin.id())).into(holder.image);
        holder.name.setText(coin.symbol());
        onBindCoinRate(holder, coin, position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            final CoinEntity payload = (CoinEntity) payloads.get(0);
            onBindCoinRate(holder, payload, position);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }


    private void onBindCoinRate(@NonNull ViewHolder holder, @NonNull CoinEntity coin, int position) {
        holder.price.setText(priceFormat.format(coin.price()));
        holder.change.setText(changeFormat.format(coin.change24()));

        final Resources resources = holder.itemView.getResources();
        final Resources.Theme theme = holder.itemView.getContext().getTheme();
        final int color;
        if (coin.change24() < 0) {
            color = ResourcesCompat.getColor(resources, R.color.watermelon, theme);
        } else {
            color = ResourcesCompat.getColor(resources, R.color.weird_green, theme);
        }
        holder.change.setTextColor(color);

        if (position%2 == 0) {
            holder.itemView.setBackgroundResource(R.color.dark_two);
        } else {
            holder.itemView.setBackgroundResource(R.color.dark_three);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final TextView name;
        final TextView price;
        final TextView change;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            change = itemView.findViewById(R.id.item_change);

            image.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth()/2);
                }
            });
            image.setClipToOutline(true);
        }
    }
}
