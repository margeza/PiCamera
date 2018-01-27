package com.example.marta.rpicamera.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marta.rpicamera.R;
import com.example.marta.rpicamera.Models.SavedItem;

import java.util.ArrayList;

/**
 * Created by Marta on 2018-01-02.
 */

public class SavedItemsAdapter extends ArrayAdapter<SavedItem> {

    public SavedItemsAdapter(Activity context, ArrayList<SavedItem> androidFlavors) {
        super(context, 0, androidFlavors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        SavedItem currentSavedItem = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.item_name);
        nameTextView.setText(currentSavedItem.getItemName());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.item_date);
        dateTextView.setText(currentSavedItem.getItemDate());

        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
        iconView.setImageResource(currentSavedItem.getImageResourceId());

        return listItemView;
    }
}
