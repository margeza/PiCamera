package com.example.marta.rpicamera.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.marta.rpicamera.Adapters.SimpleFragmentPagerAdapter;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    ImageView imageView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        ArrayList<String> listOfNames = new ArrayList<String>();
        listOfNames = intent.getStringArrayListExtra("savedItems");

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, listOfNames);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getSupportActionBar().setTitle(viewPager.getAdapter().getPageTitle(position).toString()+"  ("+Integer.toString(position+1)+"/"+Integer.toString(viewPager.getAdapter().getCount())+")");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        imageView = (ImageView) findViewById(R.id.imageView);
//
//        Picasso.with(this)
//                .load("http://192.168.1.34:5000/get_image/"+name)
//                .into(imageView);
    }
}
