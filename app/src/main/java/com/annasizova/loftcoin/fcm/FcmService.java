package com.annasizova.loftcoin.fcm;

import androidx.annotation.NonNull;

import com.annasizova.loftcoin.AppComponent;
import com.annasizova.loftcoin.R;
import com.annasizova.loftcoin.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class FcmService extends FirebaseMessagingService {

    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject FcmChannel fcmChannel;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent.from(getApplicationContext()).inject(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        final RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            disposable.add(fcmChannel.notify(
                    Objects.toString(notification.getTitle(), getString(R.string.app_name)),
                    Objects.toString(notification.getBody(), "Wtf?!"),
                    MainActivity.class
            ).subscribe());
        }
    }

    @Override
    public void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
