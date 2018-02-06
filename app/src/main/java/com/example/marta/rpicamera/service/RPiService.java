package com.example.marta.rpicamera.service;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.marta.rpicamera.Data.Results;

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

/**
 * Created by Marta on 2018-01-30.
 */

public class RPiService {

    private RPiServiceCallback callback;
    private Exception error;

    public RPiService(RPiServiceCallback callback){
        this.callback = callback;
    }

    public void refreshData(){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                String endpoint = String.format("http://89.78.145.193:5000/get_info");

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
