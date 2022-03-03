package com.creative.share.apps.wash_squad.singleton;

import android.util.Log;

import com.creative.share.apps.wash_squad.models.ItemToUpload;

import java.util.ArrayList;
import java.util.List;

public class SingleTon {

    private static SingleTon instance = null;
    private static List<ItemToUpload> itemToUploadList ;

    private SingleTon() {
    }

    public static synchronized SingleTon newInstance()
    {
        if (instance==null)
        {
            instance = new SingleTon();
            itemToUploadList= new ArrayList<>();
        }

        return instance;
    }

    public void addItem(ItemToUpload itemToUpload)
    {
        itemToUploadList.add(itemToUpload);
        Log.e("item","added");
    }

    public void removeItem(int pos)
    {
        itemToUploadList.remove(pos);
    }

    public List<ItemToUpload> getData()
    {
        return itemToUploadList;
    }

    public int getItemsCount()
    {
        return itemToUploadList.size();
    }

    public void clear()
    {
        itemToUploadList.clear();
        instance = null;

    }
}
