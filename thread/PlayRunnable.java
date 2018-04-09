package com.example.tuankiet.capstoneapp.thread;

import android.content.Context;

import com.example.tuankiet.capstoneapp.Request.HttpRequest;

/**
 * Created by tuankiet on 04/04/2018.
 */

public class PlayRunnable implements Runnable {

    private String url;
    private Context context;

    public PlayRunnable(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public void run() {

        try {
            float[] data = new HttpRequest().execute(this.url).get();
            System.out.println("PlayRunnable");
            System.out.println(data);

            if (data != null && data.length >= 1) {
                SoundRunnable soundRunnable = new SoundRunnable(data);
                Thread thread = new Thread(soundRunnable);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
