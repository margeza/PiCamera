package com.example.marta.rpicamera.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.marta.rpicamera.Data.Results;
import com.example.marta.rpicamera.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_IP;

/**
 * Created by Marta on 2018-01-30.
 */

public class RPiService {

    private RPiServiceCallback callback;
    private Exception error;
    private Context context;

    public RPiService(RPiServiceCallback callback, Context context){
        this.callback = callback;
        this.context = context;
    }

    public void refreshData(){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                Resources res = context.getResources();
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
                String cam_ip = sharedPreferences.getString(PREFS_IP, null);
                String endpoint = String.format(res.getString(R.string.get_info_url), cam_ip);

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        result.append(line);
                    }
                    return result.toString();
                }catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if(s == null && error != null){
                    callback.serviceFailure(error);
                    return;
                }
                try {
                    JSONObject data = new JSONObject(s);

                    //TODO: what if ther are no data?
//                    int count = queryResults.optInt("count");
//
//                    if (count == 0){
//                        callback.serviceFailure(new FilesRPiException("No data found"));
//                        return;
//                    }

                    Results results = new Results();
                    results.populate(data.optJSONObject("results"));

                    callback.serviceSuccess(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }.execute();
    }

    public  class FilesRPiException extends Exception{
        public FilesRPiException(String datailMessage){
            super((datailMessage));
        }
    }
}
