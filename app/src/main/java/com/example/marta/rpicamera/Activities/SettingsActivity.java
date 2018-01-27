package com.example.marta.rpicamera.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.icu.lang.UCharacter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.marta.rpicamera.R;


public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS = "preferences";
    public static final String PREFS_PASSWORD = "password";
    public static final String PREFS_USER = "userName";
    public static final String PREFS_IP = "cameraIP";

    LinearLayout camera_id_layout;
    private String m_Text = "";
    TextView camera_id_txt;
    final Context c = this;
    LinearLayout user_name_layout;
    TextView user_name_txt;
    LinearLayout password_layout;
    TextView password_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        camera_id_txt = (TextView) findViewById(R.id.camera_ip_txt);
        camera_id_layout = (LinearLayout) findViewById(R.id.camera_ip_layout);
        user_name_txt = (TextView) findViewById(R.id.user_name_txt);
        user_name_layout = (LinearLayout) findViewById(R.id.user_name_layout);
        password_txt = (TextView) findViewById(R.id.password_txt);
        password_layout = (LinearLayout) findViewById(R.id.password_layout);

        camera_id_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(
                        PREFS_IP,
                        getResources().getString(R.string.camera_IP),
                        getResources().getString(R.string.enter_ip),
                        camera_id_txt);
            }
        });

        user_name_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(
                        PREFS_USER,
                        getResources().getString(R.string.user_name),
                        getResources().getString(R.string.enter_user_name),
                        user_name_txt);
            }
        });

        password_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(
                        PREFS_PASSWORD,
                        getResources().getString(R.string.password),
                        getResources().getString(R.string.enter_password),
                        password_txt);
            }
        });

        loadData();
    }

    private void showDialog(String key ,String title, String message, TextView txt){
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        LinearLayout layout       = new LinearLayout(getApplicationContext());
        final EditText etInput    = new EditText(getApplicationContext());
        etInput.setSingleLine();
        etInput.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        etInput.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        etInput.setText(txt.getText());
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
                        txt.setText(m_Text);
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

    public void saveData(String key, String m_Text){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, m_Text);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, 0);

        String cameraID = sharedPreferences.getString(PREFS_IP, null);
        if (cameraID == null) camera_id_txt.setText(R.string.camera_URL);
        else camera_id_txt.setText(cameraID);

        String username = sharedPreferences.getString(PREFS_USER, null);
        if (username == null) user_name_txt.setText(R.string.user_name_txt);
        else user_name_txt.setText(username);

        String password = sharedPreferences.getString(PREFS_PASSWORD, null);
        if (password == null) password_txt.setText(R.string.password_txt);
        else password_txt.setText(password);

    }
}
