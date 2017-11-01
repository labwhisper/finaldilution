package com.labessence.biotech.finaldilution.peripherals.datastores;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.labessence.biotech.finaldilution.genericitem.Item;
import com.labessence.biotech.finaldilution.peripherals.DataGatewayOperations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 4/3/2017.
 */

public class SharedPreferencesStore<T extends Item> implements DataGatewayOperations<T> {

    private static final String TAG = "File DataGateway";
    //private static final File COMPOUNDS_FILE = new File("compounds.json");
    private final SharedPreferences sharedPreferences;
    private Gson gson = new GsonBuilder().create();
    private TypeToken<List<T>> listType;


    //TODO gson.toJsonTree(compound);
    // if found - load, if not create.
    // not found : no solutions.
    // solution created - saved.


    public SharedPreferencesStore(SharedPreferences sharedPreferences, TypeToken<List<T>> listType) {
        this.sharedPreferences = sharedPreferences;
        this.listType = listType;
    }

    @Override
    public void save(T item) {
        //TODO performance
        // Could use append
        // http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java

        List<T> itemList = getItemListFormPreferences();
        if (itemList.contains(item)) {
            Log.d(TAG, "Item " + item.getName() + " already present.");
            return;
        }
        itemList.add(item);
        putItemListToPreferences(itemList);
    }


    @Override
    public void update(T item) {
        List<T> itemList = getItemListFormPreferences();
        //TODO Add means to update
        if (itemList.isEmpty()) {
            return;
        }

        Iterator<T> i = itemList.iterator();
        while (i.hasNext()) {
            T currentItem = i.next();
            if (currentItem.getName().equals(item.getName())) {
                i.remove();
            }
        }

        itemList.add(item);
        putItemListToPreferences(itemList);
    }

    @Override
    public void remove(T item) {
        List<T> itemList = getItemListFormPreferences();
        if (itemList.isEmpty()) {
            return;
        }
        itemList.remove(item);
        putItemListToPreferences(itemList);
    }

    @Override
    public T load(String name) {
        List<T> itemList = getItemListFormPreferences();
        if (itemList.isEmpty()) {
            return null;
        }
        for (T item : itemList) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        //TODO Add exception at the interface to be thrown in such cases
        return null;
    }

    @Override
    public List<T> loadAll() {
        return getItemListFormPreferences();
    }

    private List<T> getItemListFormPreferences() {

        String rootJson = sharedPreferences.getString("root", "");
        List<T> loadedList = gson.fromJson(rootJson, listType.getType());

        if (loadedList == null) {
            Log.d(TAG, "Loading from preferences failed.");
            return new ArrayList<>();
        }
        return loadedList;
    }

    private void putItemListToPreferences(List<T> itemList) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String rootJson = gson.toJson(itemList);
        prefsEditor.putString("root", rootJson);
        prefsEditor.apply();
    }

}
