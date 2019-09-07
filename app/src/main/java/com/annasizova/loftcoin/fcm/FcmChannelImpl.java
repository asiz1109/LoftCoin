package com.annasizova.loftcoin.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.rx.RxSchedulers;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;

@Singleton
class FcmChannelImpl implements FcmChannel {

    private final Context context;
    private final RxSchedulers schedulers;
    private final Executor ioExecutor;
    private final NotificationManager notifications;

    @Inject
    FcmChannelImpl(Context context, RxSchedulers schedulers) {
        this.context = context;
        this.schedulers = schedulers;
        ioExecutor = schedulers.io()::scheduleDirect;
        notifications = ContextCompat.getSystemService(context, NotificationManager.class);
    }

    @NonNull
    @Override
    public Single<String> token() {
        return Single.create(emitter -> {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnSuccessListener(ioExecutor, result -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(result.getToken());
                        }
                    })
                    .addOnFailureListener(ioExecutor, emitter::tryOnError);
        });
    }

    @NonNull
    @Override
    public Completable createDefaultChannel() {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifications.createNotificationChannel(new NotificationChannel(
                        context.getString(R.string.fcm_default_channel_id),
                        context.getString(R.string.fcm_default_channel_name),
                        NotificationManager.IMPORTANCE_LOW
                ));
            }
        }).subscribeOn(schedulers.main());
    }

    @NonNull
    @Override
    public Completable notify(@NonNull String title, @NonNull String message, @NonNull Class<?> receiver) {
        return Completable
                .fromAction(() -> {
                    final String channelId = context.getString(R.string.fcm_default_channel_id);
                    final Notification notification = new NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, receiver).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_ONE_SHOT))
                            .build();
                    notifications.notify(1, notification);
                })
                .startWith(createDefaultChannel())
                .subscribeOn(schedulers.main());
    }
}
