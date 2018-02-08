package com.example.marta.rpicamera.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.marta.rpicamera.Adapters.SimpleFragmentPagerAdapter;
import com.example.marta.rpicamera.Fragments.GalleryFragment;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        ArrayList<String> listOfNames = new ArrayList<String>();
        listOfNames = intent.getStringArrayListExtra("savedItems");

        GalleryFragment galleryFragment = (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.gallery_fragment);
        galleryFragment.updateView(listOfNames,position);
    }
}
