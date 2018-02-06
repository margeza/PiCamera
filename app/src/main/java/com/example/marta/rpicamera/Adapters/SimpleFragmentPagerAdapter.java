package com.example.marta.rpicamera.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.marta.rpicamera.Fragments.CameraFragment;
import com.example.marta.rpicamera.Fragments.SavedItemsFragment;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Marta on 2018-01-02.
 */

public class SimpleFragmentPagerAdapter extends PagerAdapter {

//    final int PAGE_COUNT = 2;
//    private String tabTitles[] = new String[] { "Camera", "Saved items"};
//    private int[] imageResId = {
//            R.drawable.ic_videocam_white_24dp,
//            R.drawable.ic_playlist_play_white_24dp};
//    Context context;
//
//    public SimpleFragmentPagerAdapter(FragmentManager fm, Context c) {
//        super(fm);
//        context = c;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        if (position == 0) {
//            return new CameraFragment();
//        } else {
//            return new SavedItemsFragment();
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return PAGE_COUNT;
//    }
//
////    @Override
////    public CharSequence getPageTitle(int position) {
////        return tabTitles[position];
////    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return null;
//    }

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
//        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_insert_photo_white_24dp));
        Picasso.with(context)
                .load("http://89.78.145.193:5000/get_image/"+imagesNames.get(position))
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
