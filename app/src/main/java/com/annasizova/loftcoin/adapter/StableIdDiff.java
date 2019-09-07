package com.annasizova.loftcoin.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.annasizova.loftcoin.db.StableId;

import java.util.Objects;

public class StableIdDiff<Id, T extends StableId<Id>> extends DiffUtil.ItemCallback<T> {

    @Override
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.id() == newItem.id();
    }

    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return Objects.equals(oldItem, newItem);
    }

}
