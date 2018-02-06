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
import com.example.marta.rpicamera.MainActivity;
import com.example.marta.rpicamera.Models.SavedItem;
import com.example.marta.rpicamera.R;
import com.example.marta.rpicamera.service.RPiService;
import com.example.marta.rpicamera.service.RPiServiceCallback;

import java.util.ArrayList;

public class SavedItemsActivity extends AppCompatActivity implements RPiServiceCallback {

    Files files;
    ArrayList<SavedItem> savedItems;
    ArrayList<String> listOfNames;
    ListView listView;
    SavedItemsAdapter adapter;
    private RPiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);
        savedItems = new ArrayList<SavedItem>();
        listOfNames = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listview_saved_items);
        service = new RPiService(this);
        service.refreshData();
    }

    @Override
    public void serviceSuccess(Results results) {
        files = results.getFiles();
        updateSavedItemsList(files);
        setListOfNames(files);
        adapter = new SavedItemsAdapter(this, savedItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                Intent intent = new Intent(SavedItemsActivity.this,GalleryActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("savedItems", listOfNames);
                SavedItemsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG);
    }

    private void updateSavedItemsList(Files files){
        for(int i = 0; i < files.getDataLenght(); i++){
            savedItems.add(new SavedItem(files.getName(i), files.getDate(i), R.drawable.ic_insert_photo_white_24dp));
        }
    }

    private void setListOfNames(Files files){
        for(int i = 0; i < files.getDataLenght(); i++){
            listOfNames.add(files.getName(i));
        }
//        listOfNames.add("0.jpg");
//        listOfNames.add("1.jpg");
//        listOfNames.add("2.jpg");
    }
}
