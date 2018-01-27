package com.example.marta.rpicamera;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marta.rpicamera.Activities.AboutActivity;
import com.example.marta.rpicamera.Activities.SavedItemsActivity;
import com.example.marta.rpicamera.Activities.SettingsActivity;
import com.github.niqdev.mjpeg.MjpegView;

import java.util.HashMap;
import java.util.Map;

import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_IP;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_PASSWORD;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_USER;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback {

    private MediaPlayer _mediaPlayer;
    private SurfaceHolder _surfaceHolder;
    FloatingActionButton fab;
    Intent myIntent;
    private ProgressDialog dialog;
    private String m_Text = "";

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
                Snackbar.make(view, "Photo have been saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), getBaseContext());
//        viewPager.setAdapter(adapter);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                setFloatingButtonVisibility(position);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                setFloatingButtonVisibility(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);
//
//        int[] imageResId = {
//                R.drawable.ic_videocam_white_24dp,
//                R.drawable.ic_playlist_play_white_24dp};
//
//        for (int i = 0; i < imageResId.length; i++) {
//            tabLayout.getTabAt(i).setIcon(imageResId[i]);
//        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_raspberry_pi);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Configure the view that renders live video.
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        SurfaceView surfaceView =
                (SurfaceView) findViewById(R.id.surfaceView);
        _surfaceHolder = surfaceView.getHolder();
        _surfaceHolder.addCallback(this);
//        _surfaceHolder.setFixedSize(320, 240);
        Resources r = getResources();
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, r.getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, r.getDisplayMetrics());
        _surfaceHolder.setFixedSize(width, height);
    }


//    private void setFloatingButtonVisibility(int position){
//        if(position == 0){
//            if(fab.getVisibility() == View.INVISIBLE){
//                fab.setVisibility(View.VISIBLE);
//            }
//        }else{
//            if(fab.getVisibility() == View.VISIBLE){
//                fab.setVisibility(View.INVISIBLE);
//            }
//        }
//    }

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

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        _mediaPlayer.start();
        dialog.hide();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.setDisplay(_surfaceHolder);

        Context context = getApplicationContext();
        Map<String, String> headers = getRtspHeaders();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
        String cameraID = sharedPreferences.getString(PREFS_IP, null);
        if (cameraID == null) {
//            showDialog(PREFS_IP,
//                    getResources().getString(R.string.camera_IP),
//                    getResources().getString(R.string.enter_ip),
//                    getResources().getString(R.string.camera_URL));
            cameraID = getResources().getString(R.string.camera_URL);
        }
        Uri source = Uri.parse(cameraID);

        try {
            // Specify the IP camera's URL and auth headers.
            _mediaPlayer.setDataSource(context, source, headers);

            // Begin the process of setting up a video stream.
            _mediaPlayer.setOnPreparedListener(this);
            _mediaPlayer.prepareAsync();
        } catch (Exception e) {
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        _mediaPlayer.release();
    }

    private Map<String, String> getRtspHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
        String user = sharedPreferences.getString(PREFS_USER, null);
        String password = sharedPreferences.getString(PREFS_PASSWORD, null);
        if (user == null) {
//            showDialog(
//                    PREFS_USER,
//                    getResources().getString(R.string.user_name),
//                    getResources().getString(R.string.enter_user_name),
//                    getResources().getString(R.string.user_name_txt));
            user = "";
        }
        if (password == null) {
//            showDialog(
//                    PREFS_PASSWORD,
//                    getResources().getString(R.string.password),
//                    getResources().getString(R.string.enter_password),
//                    getResources().getString(R.string.password_txt));
            password = "";
        }
        String basicAuthValue = getBasicAuthValue(user, password);
        headers.put("Authorization", basicAuthValue);
        return headers;
    }

    private String getBasicAuthValue(String usr, String pwd) {
        String credentials = usr + ":" + pwd;
        int flags = Base64.URL_SAFE | Base64.NO_WRAP;
        byte[] bytes = credentials.getBytes();
        return "Basic " + Base64.encodeToString(bytes, flags);
    }

    private void showDialog(String key, String title, String message, String currentTxt) {
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        final EditText etInput = new EditText(getApplicationContext());
        etInput.setSingleLine();
        etInput.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        etInput.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        etInput.setText(currentTxt);
        etInput.setSelection(etInput.getText().length());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(etInput);
        layout.setPadding(50, 0, 50, 0);
        alertDialogBuilderUserInput.setView(layout);
        alertDialogBuilderUserInput.setTitle(title);
        alertDialogBuilderUserInput.setMessage(message);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        m_Text = etInput.getText().toString();
                        saveData(key, m_Text);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void saveData(String key, String m_Text) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, m_Text);
        editor.apply();
    }
}
