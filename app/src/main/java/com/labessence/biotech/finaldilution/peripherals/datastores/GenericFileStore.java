package com.labessence.biotech.finaldilution.peripherals.datastores;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.labessence.biotech.finaldilution.genericitem.Item;
import com.labessence.biotech.finaldilution.peripherals.DataGatewayOperations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 4/3/2017.
 */

public class GenericFileStore<T extends Item> implements DataGatewayOperations<T> {

    private static final String TAG = "File DataGateway";
    //private static final File COMPOUNDS_FILE = new File("compounds.json");
    private final File file;
    private Gson gson = new GsonBuilder().create();

    //TODO gson.toJsonTree(compound);
    // if found - load, if not create.
    // not found : no solutions.
    // solution created - saved.


    public GenericFileStore(File file) {
        this.file = file;
    }

    @Override
    public void save(T item) {
        //TODO performance
        // Could use append
        // http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java

        List<T> itemList = getItemListFormFile();
        if (itemList.contains(item)) {
            Log.d(TAG, "Item " + item.getName() + " already present.");
            return;
        }
        itemList.add(item);
        putItemListToFile(itemList);
    }


    @Override
    public void update(T item) {
        List<T> itemList = getItemListFormFile();
        //TODO Add means to update
        if (itemList.isEmpty()) {
            return;
        }
        itemList.remove(item);
        itemList.add(item);
        putItemListToFile(itemList);
    }

    @Override
    public void remove(T item) {
        List<T> itemList = getItemListFormFile();
        if (itemList.isEmpty()) {
            return;
        }
        itemList.remove(item);
        putItemListToFile(itemList);
    }

    @Override
    public T load(String name) {
        List<T> itemList = getItemListFormFile();
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
        return getItemListFormFile();
    }

    private List<T> getItemListFormFile() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File holding objects wasn't found.");
            return new ArrayList<>();
        }
        Type collectionType = new TypeToken<List<T>>() {
        }.getType();
        List<T> loadedList = gson.fromJson(reader, collectionType);
        if (loadedList == null) {
            return new ArrayList<>();
        }
        return loadedList;
    }

    private void putItemListToFile(List<T> itemList) {
        BufferedWriter writer = null;
        try {
            //TODO true means append if exist
            //writer = new BufferedWriter(new FileWriter(file, true));
//            if(!file.exists()){
//                file.createNewFile();
//            }
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            Log.d(TAG, "File holding objects wasn't opened properly for write.");
        }
        gson.toJson(itemList, writer);
    }

}
