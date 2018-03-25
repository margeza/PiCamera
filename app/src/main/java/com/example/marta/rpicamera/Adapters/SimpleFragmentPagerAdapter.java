package com.example.marta.rpicamera.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.marta.rpicamera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_IP;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_PASSWORD;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_USER;

/**
 * Created by Marta on 2018-01-02.
 */

public class SimpleFragmentPagerAdapter extends PagerAdapter {

    Context context;
    private ArrayList<String> imagesNames = new ArrayList<String>();

    public SimpleFragmentPagerAdapter(Context context, ArrayList<String> imagesNames){
        this.context=context;
        this.imagesNames=imagesNames;
    }

    public String getItemName(int position){
        return imagesNames.get(position);
    }

    @Override
    public int getCount() {
        return imagesNames.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resources res = context.getResources();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
        String cam_ip = sharedPreferences.getString(PREFS_IP, null);
        if(imagesNames.get(position).startsWith("vi")){
            View viewVideo = inflater.inflate(R.layout.video_layout, container, false);
            WebView webView = (WebView) viewVideo.findViewById(R.id.webViewVideo);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onReceivedHttpAuthRequest(WebView view,
                                                      HttpAuthHandler handler, String host, String realm) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
                    String user = sharedPreferences.getString(PREFS_USER, null);
                    String password = sharedPreferences.getString(PREFS_PASSWORD, null);
                    if (user == null) {user =context.getResources().getString(R.string.user_name_txt);}
                    if (password == null) {password =context.getResources().getString(R.string.password_txt);}
                    handler.proceed(user, password);

                }
            });
            String cameraID = sharedPreferences.getString(PREFS_IP, null);
            if (cameraID == null) {cameraID = context.getResources().getString(R.string.camera_ip_adres);}
            webView.loadUrl(String.format(res.getString(R.string.get_video), cam_ip)+imagesNames.get(position));
            ((ViewPager) container).addView(viewVideo, 0);
            return viewVideo;
        }else {
            View viewImage = inflater.inflate(R.layout.image_layout, container, false);
            ImageView imageView = (ImageView) viewImage.findViewById(R.id.imageView);
            Picasso.with(context)
                    .load(String.format(res.getString(R.string.get_image), cam_ip)+imagesNames.get(position))
                    .into(imageView);
            ((ViewPager) container).addView(viewImage, 0);
            return viewImage;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return getItemName(position);
    }
}
