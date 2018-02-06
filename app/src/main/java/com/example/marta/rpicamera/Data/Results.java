package com.example.marta.rpicamera.Data;

import org.json.JSONObject;

/**
 * Created by Marta on 2018-01-30.
 */

public class Results implements JSONPopulator<JSONObject> {

    private Files files;

    public Files getFiles() {
        return files;
    }

    @Override
    public void populate(JSONObject data) {
        files = new Files();
        files.populate(data.optJSONArray("files"));
    }
}
