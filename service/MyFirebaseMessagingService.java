package com.example.tuankiet.capstoneapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.tuankiet.capstoneapp.MainActivity;
import com.example.tuankiet.capstoneapp.R;
import com.example.tuankiet.capstoneapp.SingletonArrayList;
import com.example.tuankiet.capstoneapp.Sound;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by tuankiet on 06/03/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String message = "ok";
            String time = "";
            Map<String, String> data = remoteMessage.getData();
            //JSONObject jsonData;

            for (String key : data.keySet()) {
                if (key.equalsIgnoreCase("mess")) {
                        System.out.println("mess : " + data.get(key));
                        String[] arrayMessage = data.get(key).split("\n");
                        message = arrayMessage[0];
                        time = arrayMessage[1];
                        System.out.println(time);
                        String[] arrayTime = time.split(" ");
                        if (arrayTime.length > 1) {
                            time = arrayTime[0] + "T" + arrayTime[1];
                        }
                } else if (key.equalsIgnoreCase("time")) {
                    time = data.get(key);
                }
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(message)
                    .setContentText("Watch out")
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());

            //Send Request
            //String strUrl = "http://192.168.1.26:8000/getLabel/";

            try {
                // Get name
                //JSONObject label = new JSONObject(message);
                //JSONArray arrayLabel = new JSONArray(label);


                // Singleton Array list
                SingletonArrayList singletonArrayList = SingletonArrayList.getInstance();
                singletonArrayList.arrayList.get(0).setNew(false);
                singletonArrayList.arrayList.add(0,new Sound( message, time, true));
                if (singletonArrayList.arrayList.size() > 15) {
                    singletonArrayList.arrayList.remove(singletonArrayList.arrayList.size() - 1);
                }

                // Intent (Use localBroadcast to back to MainAcitivity)
                Intent intentLocalBroadcast = new Intent("receive-notification");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intentLocalBroadcast);


            }
            catch (Exception e) {
                System.out.println(e);
            }

        }


        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification());
        }
    }

    private void showNotification(RemoteMessage.Notification notification) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

}
