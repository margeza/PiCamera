package com.example.marta.rpicamera.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.example.marta.rpicamera.Fragments.CameraFragment;
import com.example.marta.rpicamera.Fragments.SavedItemsFragment;
import com.example.marta.rpicamera.R;

/**
 * Created by Marta on 2018-01-02.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Camera", "Saved items"};
    private int[] imageResId = {
            R.drawable.ic_videocam_white_24dp,
            R.drawable.ic_playlist_play_white_24dp};
    Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        context = c;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CameraFragment();
        } else {
            return new SavedItemsFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitles[position];
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
