package com.example.marta.rpicamera.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.marta.rpicamera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_IP;

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
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Resources res = context.getResources();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
        String cam_ip = sharedPreferences.getString(PREFS_IP, null);
        Picasso.with(context)
                .load(String.format(res.getString(R.string.get_image), cam_ip)+imagesNames.get(position))
                .into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return getItemName(position);
    }
}
