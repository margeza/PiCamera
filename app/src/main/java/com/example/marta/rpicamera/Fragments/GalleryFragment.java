package com.example.marta.rpicamera.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.marta.rpicamera.Adapters.SimpleFragmentPagerAdapter;
import com.example.marta.rpicamera.R;

import java.util.ArrayList;

/**
 * Created by Marta on 2018-02-07.
 */

public class GalleryFragment extends Fragment {

    SimpleFragmentPagerAdapter adapter;
    ViewPager viewPager;
    OnGalleryScrolledListener onGalleryScrolledListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setCurrentItem(1);

        return view;
    }

    public void updateView(ArrayList<String> listOfNames, int position){
        adapter = new SimpleFragmentPagerAdapter(getActivity(), listOfNames);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(viewPager.getAdapter().getPageTitle(position).toString().substring(0,7)
                        +"..." +"  (" +Integer.toString(position+1)
                        +"/"+Integer.toString(viewPager.getAdapter().getCount())  +")");
                onGalleryScrolledListener.galleryScrolled(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface OnGalleryScrolledListener{
        public void galleryScrolled(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onGalleryScrolledListener = (OnGalleryScrolledListener) context;
        }catch (Exception e){}


    }

    public ViewPager getViewPager(){
        return viewPager;
    }
}
