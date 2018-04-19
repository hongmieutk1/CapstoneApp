package com.example.tuankiet.capstoneapp;

/**
 * Created by tuankiet on 05/03/2018.
 */

public class Sound {
    private String name;
    private int sound;
    private String timeStamp;
    private boolean isNew = false;

    public Sound(String name, String timeStamp, boolean isNew) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.isNew = isNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Sound(String name, String timeStamp, int sound) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.sound = sound;
    }

    public Sound(String name, String timeStamp) {
        this.name = name;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
