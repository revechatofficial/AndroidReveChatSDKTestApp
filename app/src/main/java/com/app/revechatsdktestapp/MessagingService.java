package com.app.revechatsdktestapp;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.revesoft.revechatsdk.message.processor.PushMessageProcessor;
import org.json.JSONObject;

public class MessagingService extends FirebaseMessagingService {
    private final String TAG = "MessagingService";
    private PushMessageProcessor pushMessageProcessor;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "[MessagingService] onCreate() ");
        pushMessageProcessor = new PushMessageProcessor(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "[MessagingService] onMessageReceived data => " + remoteMessage.getData());
        pushMessageProcessor.handlePushMessage(new JSONObject(remoteMessage.getData()));
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        pushMessageProcessor.onNewToken(refreshedToken);
    }
}

