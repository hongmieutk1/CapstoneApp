package com.example.tuankiet.capstoneapp.thread;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by tuankiet on 31/03/2018.
 */

public class SoundRunnable implements Runnable {

    private float[] data;

    public SoundRunnable(float[] data) {
        this.data = data;
    }

    public void run() {
        final int sampleRate = 16000;
        final int conf = AudioFormat.CHANNEL_OUT_MONO;
        final int format = AudioFormat.ENCODING_PCM_FLOAT;
        final int mode = AudioTrack.MODE_STREAM;
        final int streamType = AudioManager.STREAM_MUSIC;

        //int bufferSize = AudioTrack.getMinBufferSize(sampleRate, conf, format);

        //System.out.println("in SoundRunnable");
        //System.out.println("buffer size : " + bufferSize);
        //System.out.println("length of data : " + data.length);
        AudioTrack track = new AudioTrack(streamType, sampleRate, conf, format, this.data.length * 4, mode);
        track.write(this.data, 0, this.data.length, AudioTrack.WRITE_BLOCKING);
        System.out.println(track.getBufferSizeInFrames());
        //System.out.println(track.getBufferCapacityInFrames());
        //track.write(data, 0, data.length);
        track.play();
        //System.out.println("done !");
    }
}
