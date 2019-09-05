package com.annasizova.loftcoin.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PagerDecoration extends RecyclerView.ItemDecoration {

    private final int dividerWidth;

    public PagerDecoration(Context context, float dip) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        dividerWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.set(dividerWidth, 0, dividerWidth, 0);
        } else {
            outRect.set(0, 0, dividerWidth, 0);
        }
    }
}
