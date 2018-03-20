package com.example.marta.rpicamera;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.marta.rpicamera.Activities.AboutActivity;
import com.example.marta.rpicamera.Activities.SavedItemsActivity;
import com.example.marta.rpicamera.Activities.SettingsActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;



import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_IP;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_PASSWORD;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_USER;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    Intent myIntent;
    String cameraID;
    String user;
    String password;
    WebView myWebView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
                        String cam_ip = sharedPreferences.getString(PREFS_IP, null);
                        if (cam_ip == null) {cam_ip = getResources().getString(R.string.camera_ip_adres);}
                        String endpoint = String.format(res.getString(R.string.photo_url), cam_ip);
                        Authenticator.setDefault (new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
                                user = sharedPreferences.getString(PREFS_USER, null);
                                password = sharedPreferences.getString(PREFS_PASSWORD, null);
                                if (user == null) {user = getResources().getString(R.string.user_name_txt);}
                                if (password == null) {password = getResources().getString(R.string.password_txt);}
                                return new PasswordAuthentication (user, password.toCharArray());
                            }
                        });

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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_raspberry_pi);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        loadWebView();
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        loadWebView();
                                    }
                                }
        );

    }

    private void loadWebView(){
        swipeRefreshLayout.setRefreshing(true);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                                                  HttpAuthHandler handler, String host, String realm) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
                user = sharedPreferences.getString(PREFS_USER, null);
                password = sharedPreferences.getString(PREFS_PASSWORD, null);
                if (user == null) {user = getResources().getString(R.string.user_name_txt);}
                if (password == null) {password = getResources().getString(R.string.password_txt);}
                handler.proceed(user, password);

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
        cameraID = sharedPreferences.getString(PREFS_IP, null);
        if (cameraID == null) {cameraID = getResources().getString(R.string.camera_ip_adres);}
        myWebView.loadUrl(String.format(getResources().getString(R.string.cam_url), cameraID));
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadWebView();
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
