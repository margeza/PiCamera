package com.example.marta.rpicamera.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marta.rpicamera.Adapters.SavedItemsAdapter;
import com.example.marta.rpicamera.Data.Files;
import com.example.marta.rpicamera.Data.Results;
import com.example.marta.rpicamera.Fragments.GalleryFragment;
import com.example.marta.rpicamera.Fragments.SavedItemsFragment;
import com.example.marta.rpicamera.MainActivity;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.R;
import com.example.marta.rpicamera.service.RPiService;
import com.example.marta.rpicamera.service.RPiServiceCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SavedItemsActivity extends AppCompatActivity implements SavedItemsFragment.OnSavedItemClickListener, GalleryFragment.OnGalleryScrolledListener {


    GalleryFragment galleryFragment;
    SavedItemsFragment savedItemsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);

        galleryFragment = (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.gallery_fragment);
        savedItemsFragment = (SavedItemsFragment) getSupportFragmentManager().findFragmentById(R.id.saved_items_fragment);
//        if(galleryFragment != null){
//            galleryFragment.getViewPager().setCurrentItem(0);
//        }
    }

    @Override
    public void savedItemSelected(ArrayList<String> listOfNames, int position) {

        if(galleryFragment == null){
            Intent intent = new Intent(SavedItemsActivity.this,GalleryActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("savedItems", listOfNames);
            SavedItemsActivity.this.startActivity(intent);

        }else {
            galleryFragment.updateView(listOfNames,position);
        }

    }

    @Override
    public void galleryScrolled(int position) {

        GalleryFragment galleryFragment = (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.gallery_fragment);
        if(galleryFragment != null){
            savedItemsFragment.getAdapter().setActualPosition(position);
            savedItemsFragment.getAdapter().notifyDataSetChanged();
        }
    }
}
