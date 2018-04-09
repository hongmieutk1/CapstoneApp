package com.example.tuankiet.capstoneapp;

import com.example.tuankiet.capstoneapp.adapter.HistoryAdapter;

import java.util.ArrayList;

/**
 * Created by tuankiet on 25/03/2018.
 */

public class SingletonArrayList {
    private static SingletonArrayList instance;

    public final ArrayList<Sound> arrayList = new ArrayList<>();
    public HistoryAdapter historyAdapter = null;


    public static SingletonArrayList getInstance() {
        if (instance == null) {
            instance = new SingletonArrayList();
        }
        return instance;
    }

    private SingletonArrayList() {

    }
}
