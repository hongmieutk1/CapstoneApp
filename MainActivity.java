package com.example.tuankiet.capstoneapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tuankiet.capstoneapp.adapter.HistoryAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //Notification
    NotificationCompat.Builder notification;
    private static final int uniqueID = 456123;

    //History
    private HistoryAdapter historyAdapter;
    private ListView soundList;

    private String timestamp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Notification
        notification = new NotificationCompat.Builder(this );
        notification.setAutoCancel(true);

        // Firebase Cloud Message
        Common.currentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("My Token", Common.currentToken);

        // Local Broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("receive-notification"));

        // Get timestamp from internal file
        //timestamp = readFromFile(this);
        //if (timestamp == "" || timestamp == null) {
        //    timestamp = getCurrentTimeStamp();
        //}
        //System.out.println("Timestamp : " + timestamp);
        //Toast.makeText(this, timestamp, Toast.LENGTH_LONG).show();

        // Get data from internal file
        SingletonArrayList singletonArrayList = SingletonArrayList.getInstance();
        readData(this);

        // History
        soundList = (ListView) findViewById(R.id.listView);

        //singletonArrayList.arrayList.add(new Sound("Gas leaking","24-3-2018", R.raw.test));

        singletonArrayList.historyAdapter = new HistoryAdapter(this, R.layout.history_item, singletonArrayList.arrayList);
        soundList.setAdapter(singletonArrayList.historyAdapter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SingletonArrayList.getInstance().historyAdapter.notifyDataSetChanged();
            startActivity(new Intent(MainActivity.this, Pop.class));
        }
    };

    @Override
    protected void onPause() {
        //writeToFile(timestamp,this);
        writeData(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        //writeToFile(timestamp, this);
        writeData(this);
        super.onDestroy();
    }

    public void showNotification(View view) {
        //SingletonArrayList.getInstance().arrayList.add(0, new Sound("Crying baby", "6/4/2018"));
        //SingletonArrayList.getInstance().historyAdapter.notifyDataSetChanged();

        //// Build the notification
        //Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //notification.setSound(sound);
        //notification.setSmallIcon(R.mipmap.ic_launcher);
        //notification.setTicker("This is the ticker");
        //notification.setWhen(System.currentTimeMillis());
        //notification.setContentTitle("The title");
        //notification.setContentText("You should learn English");


        //Intent intent = new Intent(this, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //notification.setContentIntent(pendingIntent);

        // Build notification and issues it
        //NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //notificationManager.notify(uniqueID, notification.build());

    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));

            if (outputStreamWriter == null ) {
                File file = new File(context.getFilesDir(), "config.txt");
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            }
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private void writeData(Context context) {
        //try {
        //    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data.txt", Context.MODE_PRIVATE));

        //    if (outputStreamWriter == null ) {
        //        File file = new File(context.getFilesDir(), "data.txt");
        //        System.out.println(context.getFilesDir());
        //        outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data.txt", Context.MODE_PRIVATE));
        //    }
        //    // Get String data to write file
        //    System.out.println(context.getFilesDir());
        //    String data = getData2Save();
        //    outputStreamWriter.write(data);
        //    outputStreamWriter.close();
        //}
        //catch (IOException e) {
        //    Log.e("Exception", "File write failed: " + e.toString());
        //}

        SharedPreferences sharedPref = getSharedPreferences("SomeName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("String1").commit();
        String data = getData2Save();
        while (SingletonArrayList.getInstance().arrayList.size() > 15) {
            SingletonArrayList.getInstance().arrayList.remove(SingletonArrayList.getInstance().arrayList.size()-1);
        }
        editor.putString("String1", data);  // value is the string you want to save
        editor.commit();
    }

    private String getData2Save() {
        ArrayList<Sound> arrayData = SingletonArrayList.getInstance().arrayList;
        StringBuilder data = new StringBuilder();

        for (Sound sound : arrayData) {
            data.append(sound.getName() + "," + sound.getTimeStamp() + ";");
        }

        return data.toString();
    }

    private void readData(Context context) {

        //try {
        //    InputStream inputStream = context.openFileInput("data.txt");

        //    if (inputStream != null) {
        //        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        //        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        //        String receiveString = "";
        //        StringBuilder stringBuilder = new StringBuilder();

        //        while ((receiveString = bufferedReader.readLine()) != null) {
        //            stringBuilder.append(receiveString);
        //        }

        //        inputStream.close();

        //        setData2ArrayList(receiveString);
        //    }
        //} catch (FileNotFoundException e) {
        //    Log.e("login activity", "File not found: " + e.toString());
        //} catch (IOException e) {
        //    Log.e("login activity", "Can not read file: " + e.toString());
        //}

        SharedPreferences sharedPref = getSharedPreferences("SomeName", Context.MODE_PRIVATE);
        String defaultValue="";
        String retrievedString = sharedPref.getString("String1", defaultValue);
        setData2ArrayList(retrievedString);

    }

    private void setData2ArrayList(String receiveString) {
        if (receiveString != "" && receiveString != null) {

            String[] soundArray = receiveString.split(";");
            for (String sound : soundArray) {
                if (sound != "" && sound != null ) {
                    String[] soundData = sound.split(",");
                    SingletonArrayList.getInstance().arrayList.add(new Sound(soundData[0], soundData[1]));
                }
            }
        }
    }
}
