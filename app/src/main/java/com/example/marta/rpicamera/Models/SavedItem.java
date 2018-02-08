package com.example.marta.rpicamera.Models;

/**
 * Created by Marta on 2018-01-02.
 */

public class SavedItem {

    private String mItemName;
    private String mItemDate;

    public SavedItem(String vName, String vDate)
    {
        mItemName = vName;
        mItemDate = vDate;
    }

    public String getItemName() {
        return mItemName;
    }
    public String getItemDate() {
        return mItemDate;
    }
}
