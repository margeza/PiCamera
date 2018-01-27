package com.example.marta.rpicamera.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.marta.rpicamera.R;
import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegView;

/**
 * Created by Marta on 2018-01-02.
 */

//import static com.github.niqdev.ipcam.settings.SettingsActivity.PREF_AUTH_PASSWORD;
//import static com.github.niqdev.ipcam.settings.SettingsActivity.PREF_AUTH_USERNAME;
//import static com.github.niqdev.ipcam.settings.SettingsActivity.PREF_IPCAM_URL;

public class CameraFragment extends Fragment {

    private static final int TIMEOUT = 5;
    MjpegView mjpegView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        mjpegView = (MjpegView) v.findViewById(R.id.camera_view);



        return v;
    }

    private String getPreference(String key) {
        return PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(key, "");
    }

    private DisplayMode calculateDisplayMode() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ?
                DisplayMode.FULLSCREEN : DisplayMode.BEST_FIT;
    }

    private void loadIpCam() {


        String PREF_AUTH_USERNAME = "";
        String PREF_AUTH_PASSWORD = "";
        String PREF_IPCAM_URL = "http://200.36.58.250/mjpg/video.mjpg?resolution=640x480";

        Mjpeg.newInstance()
                .credential(PREF_AUTH_USERNAME, PREF_AUTH_PASSWORD)
                .open(PREF_IPCAM_URL, TIMEOUT)
                .subscribe(
                        inputStream -> {
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(calculateDisplayMode());
                            mjpegView.showFps(true);
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIpCam();
    }

    @Override
    public void onPause() {
        super.onPause();
        mjpegView.stopPlayback();
    }

}
