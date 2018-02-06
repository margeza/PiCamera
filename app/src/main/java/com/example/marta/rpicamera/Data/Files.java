package com.example.marta.rpicamera.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Marta on 2018-01-30.
 */

public class Files implements JSONPopulator<JSONArray>{

    private ArrayList<String> nameList = new ArrayList<String>();
    private ArrayList<String> dateList = new ArrayList<String>();
    private int dataLenght;

    @Override
    public void populate(JSONArray data) {
        dataLenght = data.length();

        for(int i = 0; i <data.length(); i++){
            JSONObject file;
            try {
                file = (JSONObject) data.get(i);
                nameList.add(file.optString("name"));
                dateList.add(file.optString("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName(int i) {
        return nameList.get(i);
    }

    public String getDate(int i) {
        return dateList.get(i);
    }

    public int getDataLenght() {
        return dataLenght;
    }
}
