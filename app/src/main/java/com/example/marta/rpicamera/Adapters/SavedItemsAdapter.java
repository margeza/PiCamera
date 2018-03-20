package com.example.marta.rpicamera.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marta.rpicamera.R;
import com.example.marta.rpicamera.Models.SavedItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS;
import static com.example.marta.rpicamera.Activities.SettingsActivity.PREFS_IP;

/**
 * Created by Marta on 2018-01-02.
 */

public class SavedItemsAdapter extends ArrayAdapter<SavedItem> {

    ArrayList<SavedItem> savedItemsM = new  ArrayList<SavedItem>();
    int actualPosition;

    public SavedItemsAdapter(Activity context, ArrayList<SavedItem> savedItems, ArrayList<SavedItem> savedItemsMini) {
        super(context, 0, savedItems);
        savedItemsM = savedItemsMini;
    }

    public void setActualPosition(int i){actualPosition = i;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        SavedItem currentSavedItem = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.item_name);
        nameTextView.setText(currentSavedItem.getItemName().substring(0,7)+"...");

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.item_date);
        dateTextView.setText(currentSavedItem.getItemDate());

        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
        Resources res = getContext().getResources();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS, 0);
        String cam_ip = sharedPreferences.getString(PREFS_IP, null);
        Picasso.with(getContext())
                .load(String.format(res.getString(R.string.get_image), cam_ip)+savedItemsM.get(position).getItemName())
                .into(iconView);

        if ((getContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            if (position == actualPosition) {
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightGrey));
            } else {
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.colorItem));
            }
        }

        return listItemView;
    }
}
