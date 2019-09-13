package com.annasizova.loftcoin.rx;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.MainThreadDisposable;

public class RxRecyclerView {

    @NonNull
    public static Observable<View> onItemClick(@NonNull RecyclerView rv) {
        return Observable.create(emitter -> {
            MainThreadDisposable.verifyMainThread();
            final RecyclerView.OnItemTouchListener listener = new ItemClickHelper(rv.getContext(), emitter);
            emitter.setCancellable(() -> rv.removeOnItemTouchListener(listener));
            rv.addOnItemTouchListener(listener);
        });
    }

    private static class ItemClickHelper implements RecyclerView.OnItemTouchListener {

        private final GestureDetectorCompat gestureDetector;
        private final ObservableEmitter<View> emitter;

        ItemClickHelper(Context context, ObservableEmitter<View> emitter) {
            gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
            this.emitter = emitter;
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            final View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && gestureDetector.onTouchEvent(e) && !emitter.isDisposed()) {
                emitter.onNext(child);
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    }
}
