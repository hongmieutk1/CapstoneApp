package com.example.tuankiet.capstoneapp.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuankiet.capstoneapp.R;
import com.example.tuankiet.capstoneapp.Request.HttpRequest;
import com.example.tuankiet.capstoneapp.Sound;
import com.example.tuankiet.capstoneapp.thread.PlayRunnable;
import com.example.tuankiet.capstoneapp.thread.SoundRunnable;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.System.*;

/**
 * Created by tuankiet on 05/03/2018.
 */

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Sound> arrayList;
    private MediaPlayer mediaPlayer;

    public HistoryAdapter(Context context, int layout, ArrayList<Sound> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public ArrayList<Sound> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Sound> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView soundName;
        TextView timeStamp;
        ImageView ivPlay, ivStop;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(layout, null);
            viewHolder.soundName = (TextView) view.findViewById(R.id.soundName);
            viewHolder.timeStamp = (TextView) view.findViewById(R.id.timeStamp);
            viewHolder.ivPlay = (ImageView) view.findViewById(R.id.ivPlay);
            //viewHolder.ivStop = (ImageView) view.findViewById(R.id.ivStop);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Sound sound = arrayList.get(i);
        viewHolder.soundName.setText(sound.getName());
        viewHolder.timeStamp.setText(sound.getTimeStamp());

        // Play music
        viewHolder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //Send Request
                //String strUrl = "http://192.168.1.26:8000/getSound?time_start=";
                String baseUrl = "http://172.20.10.3:8000/label/getSound?time_start=";
                String stringUserID = "&user_id=1";
                //String strUrl = "http://172.29.192.70:8000/label/getSound?time_start=2017-12-21T08:10:01&user_id=1";

                //String date = "2017-12-21 15:10:01+07";
                String date = sound.getTimeStamp();
                //String dateTest = dateFormat(date);
                //String[] split = dateTest.split(" ");
                //baseUrl += split[0] + "T" + split[1] + stringUserID;
                baseUrl += date + stringUserID;
                out.println("baseURl " + baseUrl );

                try {
                    // Get data + run Sound in THREAD
                    //PlayRunnable playRunnable = new PlayRunnable(getContext(), baseUrl);
                    //Thread thread = new Thread(playRunnable);
                    //thread.start();

                    float[] data = new HttpRequest().execute(baseUrl).get();
                    System.out.println("PlayRunnable");
                    System.out.println(data);

                    if (data != null && data.length >= 1) {
                        SoundRunnable soundRunnable = new SoundRunnable(data);
                        Thread thread = new Thread(soundRunnable);
                        thread.start();
                    }

                    Toast.makeText(context, "Loading... Please wait !!",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    out.println(e);
                }
            }
        });

        return view;
    }

    private byte[] FloatArray2ByteArray(float[] dataWave) {
        ByteBuffer waveByte = ByteBuffer.allocate(4 * dataWave.length);
        for (float temp : dataWave) {
            waveByte.putFloat(temp);
        }

        return waveByte.array();
    }

    private String dateFormat(String date) {

        String[] stringDate = date.split("/+");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //Calendar temp = Calendar.getInstance();
            Date time = sdf.parse(stringDate[0]);
            TimeZone tz = TimeZone.getTimeZone("GMT+07");
            //temp.setTimeZone(tz);
            Long milisecond = time.getTime();
            milisecond = milisecond - 25200000;
            Date date1 = new Date(milisecond);
            DateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            test.setTimeZone(tz);
            //test.format(date1);
            out.println(date1);
            String testing = test.format(date1);
            return testing;
            //System.out.println(date1.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
