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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class RateAdapter extends ListAdapter <CoinRate, RateAdapter.ViewHolder> {

    private LayoutInflater inflater;

    RateAdapter() {
        super(new DiffUtil.ItemCallback<CoinRate>() {
            @Override
            public boolean areItemsTheSame(@NonNull CoinRate oldItem, @NonNull CoinRate newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CoinRate oldItem, @NonNull CoinRate newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_rate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CoinRate rate = getItem(position);
        Picasso.get().load(rate.imageUrl()).into(holder.image);
        holder.name.setText(rate.symbol());
        holder.price.setText(rate.price());
        holder.change.setText(rate.change24h());

        final Resources resources = holder.itemView.getResources();
        final Resources.Theme theme = holder.itemView.getContext().getTheme();
        final int color;
        if (rate.isChange24hNegative()) {
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

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
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
