package com.example.marta.rpicamera.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marta.rpicamera.Activities.GalleryActivity;
import com.example.marta.rpicamera.Activities.SavedItemsActivity;
import com.example.marta.rpicamera.Data.Files;
import com.example.marta.rpicamera.Data.Results;
import com.example.marta.rpicamera.R;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.Adapters.SavedItemsAdapter;
import com.example.marta.rpicamera.service.RPiService;
import com.example.marta.rpicamera.service.RPiServiceCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Marta on 2018-01-02.
 */

public class SavedItemsFragment extends Fragment implements RPiServiceCallback, SwipeRefreshLayout.OnRefreshListener {

    Files files;
    ArrayList<SavedItem> savedItems;
    ArrayList<SavedItem> savedItemsMini;
    ArrayList<String> listOfNames;
    ListView listView;
    SavedItemsAdapter adapter;
    private RPiService service;
    OnSavedItemClickListener onSavedItemClickListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_items, container, false);

        savedItems = new ArrayList<SavedItem>();
        savedItemsMini = new ArrayList<SavedItem>();
        listOfNames = new ArrayList<String>();
        listView = (ListView) view.findViewById(R.id.listview_saved_items);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        service = new RPiService(this);
        service.refreshData();
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        service.refreshData();
                                    }
                                }
        );

        return view;
    }

    @Override
    public void serviceSuccess(Results results) {
        files = results.getFiles();
        if(savedItems!=null){
            savedItems.clear();
            savedItemsMini.clear();
            listOfNames.clear();
        }
        updateSavedItemsList(files);
        adapter = new SavedItemsAdapter(getActivity(), savedItems, savedItemsMini);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                adapter.setActualPosition(position);
                adapter.notifyDataSetChanged();
                onSavedItemClickListener.savedItemSelected(listOfNames,position);
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG);
    }

    private void updateSavedItemsList(Files files){
        for(int i = 0; i < files.getDataLenght(); i++){
            if(files.getName(i).length() <= 27) {
                savedItems.add(new SavedItem(files.getName(i), files.getDate(i)));
                listOfNames.add(files.getName(i));
            }else {
                savedItemsMini.add(new SavedItem(files.getName(i), files.getDate(i)));
            }
        }
        Collections.sort(savedItems, new SavedItemsNameComparator());
        Collections.reverse(savedItems);
        Collections.sort(savedItemsMini, new SavedItemsNameComparator());
        Collections.reverse(savedItemsMini);
        Collections.sort(listOfNames);
        Collections.reverse(listOfNames);
    }

    @Override
    public void onRefresh() {
        service.refreshData();
    }

    public class SavedItemsNameComparator implements Comparator<SavedItem>
    {
        public int compare(SavedItem left, SavedItem right) {
            return left.getItemName().compareTo(right.getItemName());
        }
    }

    public interface OnSavedItemClickListener{
        public void savedItemSelected(ArrayList<String> listOfNames, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onSavedItemClickListener = (OnSavedItemClickListener) context;
        }catch (Exception e){}


    }

    public SavedItemsAdapter getAdapter(){
        return adapter;
    }
    public ListView getListView(){return listView;}
}
