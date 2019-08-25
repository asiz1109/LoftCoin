package com.annasizova.loftcoin.welcome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annasizova.loftcoin.R;

public class WelcomeAdapter extends RecyclerView.Adapter<WelcomeAdapter.WelcomeViewHolder> {

    private LayoutInflater inflater;

    private static final int[] IMAGES = {
            R.drawable.welcome_page_1,
            R.drawable.welcome_page_2,
            R.drawable.welcome_page_3
    };

    private static final int[] TITLES = {
            R.string.welcome_page_1_title,
            R.string.welcome_page_2_title,
            R.string.welcome_page_3_title
    };

    private static final int[] SUBTITLES = {
            R.string.welcome_page_1_subtitle,
            R.string.welcome_page_2_subtitle,
            R.string.welcome_page_3_subtitle
    };

    @NonNull
    @Override
    public WelcomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WelcomeViewHolder(inflater.inflate(R.layout.welcome_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeViewHolder holder, int position) {
        holder.welcome_image.setImageResource(IMAGES[position]);
        holder.welcome_title.setText(TITLES[position]);
        holder.welcome_subtitle.setText(SUBTITLES[position]);
    }

    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class WelcomeViewHolder extends RecyclerView.ViewHolder {

        ImageView welcome_image;
        TextView welcome_title, welcome_subtitle;

        public WelcomeViewHolder(@NonNull View itemView) {
            super(itemView);
            welcome_image = itemView.findViewById(R.id.welcome_image);
            welcome_title = itemView.findViewById(R.id.welcome_title);
            welcome_subtitle = itemView.findViewById(R.id.welcome_subtitle);
        }
    }
}
