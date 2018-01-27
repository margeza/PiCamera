package com.example.marta.rpicamera.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.marta.rpicamera.Adapters.SavedItemsAdapter;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.R;

import java.util.ArrayList;

public class SavedItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);

        ArrayList<SavedItem> savedItems = new ArrayList<SavedItem>();
        savedItems.add(new SavedItem("Video 1", "2018-01-02 20:17", R.drawable.ic_video_library_white_24dp));
        savedItems.add(new SavedItem("Photo 1", "2018-01-02 20:12", R.drawable.ic_insert_photo_white_24dp));
        savedItems.add(new SavedItem("Video 2", "2018-01-02 18:52", R.drawable.ic_video_library_white_24dp));
        savedItems.add(new SavedItem("Photo 2", "2018-01-02 18:45", R.drawable.ic_insert_photo_white_24dp));
        savedItems.add(new SavedItem("Video 3", "2018-01-02 17:05", R.drawable.ic_video_library_white_24dp));
        savedItems.add(new SavedItem("Video 4", "2018-01-01 12:11", R.drawable.ic_video_library_white_24dp));
        savedItems.add(new SavedItem("Video 5", "2018-01-01 10:54", R.drawable.ic_video_library_white_24dp));
        savedItems.add(new SavedItem("Photo 3", "2018-01-01 10:13", R.drawable.ic_insert_photo_white_24dp));
        savedItems.add(new SavedItem("Video 6", "2017-12-31 19:02", R.drawable.ic_video_library_white_24dp));
        savedItems.add(new SavedItem("Photo 4", "2017-12-31 18:34", R.drawable.ic_insert_photo_white_24dp));

        SavedItemsAdapter savedItemsAdapter = new SavedItemsAdapter(this, savedItems);
        ListView listView = (ListView) findViewById(R.id.listview_saved_items);
        listView.setAdapter(savedItemsAdapter);
    }
}
