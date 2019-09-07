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
    private final int screenWidth;

    public PagerDecoration(Context context, float dip) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        dividerWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm));
        screenWidth = dm.widthPixels;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        final int viewHalfWidth = view.getLayoutParams().width/2;
        final int screenHalfWidth = screenWidth/2;
        if (position == 0) {
            outRect.set(screenHalfWidth - viewHalfWidth, 0, dividerWidth, 0);
        }
        else if (position == state.getItemCount()-1) {
            outRect.set(0, 0, screenHalfWidth - viewHalfWidth, 0);
        }
        else {
            outRect.set(0, 0, dividerWidth, 0);
        }
    }
}
