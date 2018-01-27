package com.example.marta.rpicamera.Models;

/**
 * Created by Marta on 2018-01-02.
 */

public class SavedItem {

    private String mItemName;
    private String mItemDate;
    private int mImageResourceId;

    public SavedItem(String vName, String vDate, int imageResourceId)
    {
        mItemName = vName;
        mItemDate = vDate;
        mImageResourceId = imageResourceId;
    }

    public String getItemName() {
        return mItemName;
    }
    public String getItemDate() {
        return mItemDate;
    }
    public int getImageResourceId() {
        return mImageResourceId;
    }
}
