package com.creative.share.apps.wash_squad.notification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.models.NotStateModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.newInstance();
    private Map<String, String> map;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            map = remoteMessage.getData();
            for (String key : map.keySet()) {
                Log.e("Key :" + key, "__ value :" + map.get(key));
            }

            if (getSession().equals(Tags.session_login)) {
                manageNotification();
            }
        }

    }

    private void manageNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotificationNew();
        } else {
            sendNotificationOld();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationNew() {
        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        String not_type = map.get("message_key");
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        String class_path = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        Log.e("llllll", class_path);
        if (class_path.equals("com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.OrderDetailsActivity")) {
            if (not_type.equals("new_order") || not_type.equals("driver_start_work") || not_type.equals("driver_end_work")) {


                NotStateModel model = new NotStateModel(not_type);
                EventBus.getDefault().post(model);
            }
        }
//        else {
//            if (not_type.equals("new_order") || not_type.equals("driver_start_work") || not_type.equals("driver_end_work")){
//                String CHANNEL_ID = "my_channel_02";
//                CharSequence CHANNEL_NAME = "my_channel_name";
//                int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
//
//                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
//                channel.setShowBadge(true);
//                channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
//                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
//                        .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
//                        .build()
//                );
//                builder.setChannelId(CHANNEL_ID);
//                builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
//                builder.setSmallIcon(R.mipmap.ic_notification);
//                builder.setContentTitle(getResources().getString(R.string.new_order));
//
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("data", "order");
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(pendingIntent);
//                builder.setContentText(getResources().getString(R.string.new_order));
//                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                if (manager != null) {
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_only);
//                    builder.setLargeIcon(bitmap);
//                    manager.createNotificationChannel(channel);
//                    manager.notify(new Random().nextInt(200), builder.build());
//                }
//            }
//        }

    }

    private void sendNotificationOld() {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        String not_type = map.get("message_key");
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        String class_path = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        if (class_path.equals("com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.OrderDetailsActivity")) {
            if (not_type.equals("new_order") || not_type.equals("driver_start_work") || not_type.equals("driver_end_work")) {


                NotStateModel model = new NotStateModel(not_type);
                EventBus.getDefault().post(model);
            }
        }
//        else {
//            if (not_type.equals("new_order") || not_type.equals("driver_start_work") || not_type.equals("driver_end_work")){
//                String CHANNEL_ID = "my_channel_02";
//                CharSequence CHANNEL_NAME = "my_channel_name";
//                int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
//
//                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//                builder.setChannelId(CHANNEL_ID);
//                builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
//                builder.setSmallIcon(R.mipmap.ic_notification);
//                builder.setContentTitle(getResources().getString(R.string.new_order));
//
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("data", "order");
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(pendingIntent);
//                builder.setContentText(getResources().getString(R.string.new_order));
//                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                if (manager != null) {
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_only);
//                    builder.setLargeIcon(bitmap);
//                    manager.notify(new Random().nextInt(200), builder.build());
//                }
//            }
//        }


    }

    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getSession() {
        return preferences.getSession(this);
    }
}
