package com.app.noan.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.app.noan.R;
import com.app.noan.activity.ConfirmSoldeActivity;
import com.app.noan.activity.HomeActivity;
import com.app.noan.activity.IncompleteListActivity;
import com.app.noan.activity.ListingActivity;
import com.app.noan.activity.OrderDetailActivity;
import com.app.noan.activity.ProductDetailsActivity;
import com.app.noan.app.Config;
import com.app.noan.model.NeedToConfirmModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by smn on 18/1/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        //"Title","Message","NotyType",   "hotelStatus"

        String title = "";
        if (remoteMessage.getNotification().getTitle() != null) {
            title = remoteMessage.getNotification().getTitle();
        } else {
            title = "Noan";
        }

        String message = "";
        if (remoteMessage.getNotification().getBody() != null) {
            message = remoteMessage.getNotification().getBody();
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());
            JSONObject jsonObjectItem = null;
            jsonObjectItem = new JSONObject(remoteMessage.getData());
            handleDataMessage(title, message, jsonObjectItem);
        }

        Log.d(TAG, "size" + remoteMessage.getData().size());


    }


 /*   private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
*/

    public void handleDataMessage(String title, String message, JSONObject jsonObjectItem) {
        Intent intent = null;
        Bundle bundle;
        String pushType = null;
        String data;
        JSONObject mJsonObject = null;
        try {
            Gson gson = new Gson();
            pushType = jsonObjectItem.getString("push_type");
            data = jsonObjectItem.getString("data");
            try {
                mJsonObject = new JSONObject(data);
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + data + "\"");
            }

            Log.d("push_type", pushType);
            if (pushType != null) {
                if (pushType.equals("SANDAL SIZE AVAILABLE")) {
                    intent = new Intent(MyFirebaseMessagingService.this, ProductDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    bundle = new Bundle();
                    bundle.putString("product_id", mJsonObject.getString("product_id"));
                    intent.putExtras(bundle);
                } else if (pushType.equals("SANDAL SHIPPED TO YOU")) {
                    NeedToConfirmModel needToConfirmModel = gson.fromJson(data, NeedToConfirmModel.class);
                    intent = new Intent(MyFirebaseMessagingService.this, OrderDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    bundle = new Bundle();
                    intent.putExtra("NeedConfirmModel", (Serializable) needToConfirmModel);
                    intent.putExtras(bundle);

                } else if (pushType.equals("SANDAL DELIVERED TO YOU")) {
                    NeedToConfirmModel needToConfirmModel = gson.fromJson(data, NeedToConfirmModel.class);
                    intent = new Intent(MyFirebaseMessagingService.this, OrderDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    bundle = new Bundle();
                    intent.putExtra("NeedConfirmModel", (Serializable) needToConfirmModel);
                    intent.putExtras(bundle);

                } else if (pushType.equals("UPDATE ON YOUR OFFERS")) {
                    NeedToConfirmModel needToConfirmModel = gson.fromJson(data, NeedToConfirmModel.class);
                    intent = new Intent(MyFirebaseMessagingService.this, OrderDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    bundle = new Bundle();
                    intent.putExtra("NeedConfirmModel", (Serializable) needToConfirmModel);
                    intent.putExtras(bundle);

                } else if (pushType.equals("YOUR SANDAL SOLD")) {
                    NeedToConfirmModel needToConfirmModel = gson.fromJson(data, NeedToConfirmModel.class);
                    intent = new Intent(MyFirebaseMessagingService.this, ConfirmSoldeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    bundle = new Bundle();
                    intent.putExtra("NeedToConfirmModel", (Serializable) needToConfirmModel);
                    intent.putExtras(bundle);

                } else if (pushType.equals("UPDATE ON YOUR SELLER ACCOUNT STATUS")) {
                    intent = new Intent(MyFirebaseMessagingService.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    bundle = new Bundle();
                    intent.putExtra("Notification", "isNotification");
                    intent.putExtras(bundle);
                } else if (pushType.equals("UPDATE ON YOUR LISTED PRODUCT FOR SELL")) {
                    String status = mJsonObject.getString("status");
                    if (status.equals("approved")) {
                        intent = new Intent(MyFirebaseMessagingService.this, ListingActivity.class);
                    } else {
                        intent = new Intent(MyFirebaseMessagingService.this, IncompleteListActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    bundle = new Bundle();
                    intent.putExtras(bundle);
                } else {
                    intent = new Intent(MyFirebaseMessagingService.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    bundle = new Bundle();
                    intent.putExtras(bundle);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        final int icon = R.mipmap.app_launcher;
        inboxStyle.addLine(message);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.app_launcher)
                /*.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_launcher))*/
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(getResources().getColor(R.color.colorAccent))
                .build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }


}
