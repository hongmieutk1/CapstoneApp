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

            normalizeAudio(data);

            if (data != null && data.length >= 1) {
                SoundRunnable soundRunnable = new SoundRunnable(data);
                Thread thread = new Thread(soundRunnable);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void normalizeAudio(float[] data) {

        float max = findMax(data);
        System.out.println("max : " + max);

        float time = (1 / max) > 2 ? (1 / max) - 1 : (1/max);
        System.out.println("time : " + time);

        normalizeArrayAudio(data, time);
        max = findMax(data);
        System.out.println("max : " + max);
    }

    private float findMax(float[] data) {
        float max = Float.MIN_VALUE;

        for (float i : data) {
            max = Math.max(max, i);
        }

        return max;
    }

    private void normalizeArrayAudio(float[] data, float time) {
        for (int i = 0; i < data.length; i++) {
            data[i] *= time;
        }
    }
}
