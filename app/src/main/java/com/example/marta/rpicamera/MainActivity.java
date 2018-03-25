package com.example.marta.rpicamera;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marta.rpicamera.Activities.AboutActivity;
import com.example.marta.rpicamera.Activities.SavedItemsActivity;
import com.example.marta.rpicamera.Activities.SettingsActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;


import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_IP;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_PASSWORD;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_USER;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String PREFS_MOTION = "motionStatus";
    private static int START = 1;
    private static int STOP = 0;
    FloatingActionButton fab;
    FloatingActionButton motion;
    Intent myIntent;
    String cameraIP;
    String user;
    String password;
    WebView myWebView;
    private SwipeRefreshLayout swipeRefreshLayout;
    int motion_status;
    Exception error;
    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(PREFS_IP)){
                cameraIP = sharedPreferences.getString(PREFS_IP, getString(R.string.camera_IP));
            } else if (key.equals(PREFS_USER)) {
                user = sharedPreferences.getString(PREFS_USER, getString(R.string.user_name_txt));
            } else if (key.equals(PREFS_PASSWORD)){
                password = sharedPreferences.getString(PREFS_PASSWORD, getString(R.string.password_txt));
            }
        };
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... strings) {

                        Resources res = getResources();
                        String endpoint = String.format(res.getString(R.string.photo_url), cameraIP);
                        setAuthentication();

                        try {
                            URL url = new URL(endpoint);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.connect();
                            int response = connection.getResponseCode();
                            Log.d("cam", "Response from server: " + response);
                            if (response == 200) {
                                Snackbar.make(view, getResources().getString(R.string.saved_photo), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else {
                                Snackbar.make(view, getResources().getString(R.string.not_saved_photo), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        }catch (Exception e) {
                            Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        return null;
                    }
                }.execute();
            }
        });

        motion = (FloatingActionButton) findViewById(R.id.motion);
        motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                motion(view);
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_raspberry_pi);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        initValuesFromSharedPreferences();
        loadWebView();
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }
        );

        FirebaseMessaging.getInstance().subscribeToTopic("motion");
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("NOTIFICATION", "Key: " + key + " Value: " + value);
                if(value.equals("/topics/motion")){
                    Log.d("NOTIFICATION", "Inside!");
                    motion(motion);
                }else {currentMotionStatus();}
            }
        }else {currentMotionStatus();}
    }

    private void motion(View view){
        new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... strings) {
                Resources res = getResources();
                String endpoint;
                updateMotionDetectionState();
                int newMotionStatus = getOppositeMotionStatus();
                endpoint = String.format(res.getString(R.string.motion_url), cameraIP)+ Integer.toString(newMotionStatus);
                setAuthentication();
                try {
                    URL url = new URL(endpoint);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int response = connection.getResponseCode();
                    Log.d("cam", "Response from server: " + response);
                    if (response == 200) {
                        if(newMotionStatus == START){
                            Snackbar.make(view, getResources().getString(R.string.motion_start), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else {
                            Snackbar.make(view, getResources().getString(R.string.motion_stop), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        return newMotionStatus;
                    }else {
                        Snackbar.make(view, getResources().getString(R.string.not_saved_photo), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }catch (Exception e) {
                    Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                return -1;
            }

            @Override
            protected void onPostExecute(Integer newMotionStatus) {
                super.onPostExecute(newMotionStatus);
                motion_status = newMotionStatus;
                setColorOfFloatingButton(newMotionStatus);

            }
        }.execute();
    }

    private void initValuesFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        cameraIP = preferences.getString(PREFS_IP, getString(R.string.camera_ip_adres));
        user = preferences.getString(PREFS_USER, getString(R.string.user_name_txt));
        password = preferences.getString(PREFS_PASSWORD, getString(R.string.password_txt));
    }

    private void setAuthentication(){
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (user, password.toCharArray());
            }
        });
    }
    private void currentMotionStatus(){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return updateMotionDetectionState();
            }
            @Override
            protected void onPostExecute(String s) {
                setColorOfFloatingButton(motion_status);
            }
        }.execute();
    }

    private String updateMotionDetectionState(){
        Resources res = getResources();
        String endpoint;
        endpoint = String.format(res.getString(R.string.motion_status), cameraIP);
        setAuthentication();

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                result.append(line);
            }
            Log.d(PREFS_MOTION, "Response from server: " + result.toString());
            if (result.toString().equals("ready")) {
                motion_status = STOP;
            }else {
                motion_status = START;
            }
            return result.toString();

        }catch (Exception e) {
            Log.e(PREFS_MOTION, e.toString());
            error = e;
        }
        return null;
    }

    public void setColorOfFloatingButton(int a){
        if(a == START){
            motion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));}
        else {
            motion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));}
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSharedPreferences(PREFS, 0).registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        currentMotionStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences(PREFS, 0).unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentMotionStatus();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        currentMotionStatus();
    }

    private int getOppositeMotionStatus(){
        if(motion_status == STOP){
            return START;}
        else {
            return STOP;}
    }

    private void loadWebView(){
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                                                  HttpAuthHandler handler, String host, String realm) {
                handler.proceed(user, password);

            }
        });
        myWebView.loadUrl(String.format(getResources().getString(R.string.cam_url), cameraIP));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadWebView();
        currentMotionStatus();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        } else if (id == R.id.action_about) {
            myIntent = new Intent(MainActivity.this, AboutActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        } else if (id == R.id.action_saved_items) {
            myIntent = new Intent(MainActivity.this, SavedItemsActivity.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
