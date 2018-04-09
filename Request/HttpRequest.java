package com.example.tuankiet.capstoneapp.Request;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Created by tuankiet on 25/03/2018.
 */

public class HttpRequest extends AsyncTask<String, Void, float[]> {

    private ProgressDialog progressDialog;

    public HttpRequest() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = new ProgressDialog(this.context);
        //progressDialog.setCancelable(true);
        //progressDialog.setMessage("Loading...");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setProgress(0);
        //progressDialog.show();
        //Toast.makeText(context, "Loading... Please wait !!",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(float[] o) {
        //Handler handler =  new Handler(Looper.getMainLooper());
        //handler.post( new Runnable(){
        //    public void run(){
        //        Toast.makeText(this, "Created a server socket",Toast.LENGTH_LONG).show();
        //    }
        //});
        super.onPostExecute(o);
    }

    @Override
    protected float[] doInBackground(String... strings) {
        try {

            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String value;

            while ((value = bf.readLine()) != null) {
                stringBuffer.append(value);
            }

            System.out.println("pre data");

            JSONObject data = new JSONObject(stringBuffer.toString());
            System.out.println("Data");

            JSONArray result = data.getJSONArray("result");
            System.out.println("result");

            JSONObject resultArray = result.getJSONObject(0);

            //String time_start = resultArray.getString("time_start");
            //String sr = resultArray.getString("sr");
            //String label = resultArray.getString("label");
            //String wave = resultArray.getString("wave");

            JSONArray wave = resultArray.getJSONArray("wave");

            float[] dataWave = new float[wave.length()];
            for (int i = 0; i < wave.length(); i++) {
                try {
                    dataWave[i] = Float.parseFloat(wave.getString(i));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return dataWave;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    private byte[] FloatArray2ByteArray(float[] dataWave) {
        ByteBuffer waveByte = ByteBuffer.allocate(4 * dataWave.length);
        for (float temp : dataWave) {
            waveByte.putFloat(temp);
        }

        return waveByte.array();
    }
}
