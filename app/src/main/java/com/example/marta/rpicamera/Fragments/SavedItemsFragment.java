package com.example.marta.rpicamera.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.marta.rpicamera.R;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.Adapters.SavedItemsAdapter;

import java.util.ArrayList;

/**
 * Created by Marta on 2018-01-02.
 */

public class SavedItemsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_items, container, false);

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

        SavedItemsAdapter savedItemsAdapter = new SavedItemsAdapter(getActivity(), savedItems);
        ListView listView = (ListView) view.findViewById(R.id.listview_saved_items);
        listView.setAdapter(savedItemsAdapter);
        return view;
    }
}
