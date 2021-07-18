package com.eatyhero.customer.fcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.eatyhero.customer.base.NewBaseHomeScreen;
import com.eatyhero.customer.common.MyApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = com.eatyhero.customer.fcm.MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    String type = "";
    JSONObject payload;
    String message="";

    //Get UserId
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userid="";
    private MyApplication application;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());



        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();

        } else {
            // If the app is in background, firebase itself handles the notification

        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "json Response: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title         = data.getString("title");
            message       = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String timestamp      = data.getString("timestamp");
            payload               = data.getJSONObject("payload");
            //type                  = payload.getString("type");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("update", true);
                pushNotification.putExtra("message", message);

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(), title, message, timestamp, new Intent(getApplicationContext(), NewBaseHomeScreen.class));


            } else if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in background, show the notification in notification tray
               /* Intent resultIntent = new Intent(getApplicationContext(), OrdersPage.class);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("OrderDetails", OrderDetails.toString());
                resultIntent.putExtra("type", type);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);*/

                Log.e("22222222","2222222222");

                Intent resultIntent = new Intent(getApplicationContext(), NewBaseHomeScreen.class);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);

            } else {

                Log.e("333333","33333333333");

                Intent resultIntent = new Intent(getApplicationContext(), NewBaseHomeScreen.class);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage("Pintogogo Customer", message, timeStamp, intent);
    }


}
