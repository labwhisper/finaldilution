package com.labwhisper.biotech.finaldilution.peripherals.datastores

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.labwhisper.biotech.finaldilution.genericitem.Item
import com.labwhisper.biotech.finaldilution.peripherals.DataGatewayOperations
import java.io.*
import java.util.*

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 4/3/2017.
 */

class GenericFileStore<T : Item>
//TODO gson.toJsonTree(compound);
// if found - load, if not create.
// not found : no solutions.
// solution created - saved.


    (//private static final File COMPOUNDS_FILE = new File("compounds.json");
    private val file: File
) : DataGatewayOperations<T> {

    private val gson = GsonBuilder().create()

    private val itemListFormFile: MutableList<T>
        get() {
            val reader: BufferedReader?
            try {
                reader = BufferedReader(FileReader(file))
            } catch (e: FileNotFoundException) {
                Log.d(TAG, "File holding objects wasn't found.")
                return ArrayList()
            }

            val collectionType = object : TypeToken<MutableList<T>>() {

            }.type
            return gson.fromJson<MutableList<T>>(reader, collectionType) ?: return ArrayList()
        }

    override fun size() = itemListFormFile.size

    override fun save(item: T) {
        //TODO performance
        // Could use append
        // http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java

        val itemList = itemListFormFile
        if (itemList.contains(item)) {
            Log.d(TAG, "Item " + item.name + " already present.")
            return
        }
        itemList.add(item)
        putItemListToFile(itemList)
    }

    override fun rename(item: T, oldName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(item: T) {
        val itemList = itemListFormFile
        //TODO Add means to update
        if (itemList.isEmpty()) {
            return
        }
        itemList.remove(item)
        itemList.add(item)
        putItemListToFile(itemList)
    }

    override fun remove(item: T) {
        val itemList = itemListFormFile
        if (itemList.isEmpty()) {
            return
        }
        itemList.remove(item)
        putItemListToFile(itemList)
    }

    override fun load(name: String): T? {
        val itemList = itemListFormFile
        if (itemList.isEmpty()) {
            return null
        }
        for (item in itemList) {
            if (item.name == name) {
                return item
            }
        }
        //TODO Add exception at the interface to be thrown in such cases
        return null
    }

    override fun loadAll(): List<T> {
        return itemListFormFile
    }

    private fun putItemListToFile(itemList: List<T>) {
        var writer: BufferedWriter? = null
        try {
            //TODO true means append if exist
            //writer = new BufferedWriter(new FileWriter(file, true));
            //            if(!file.exists()){
            //                file.createNewFile();
            //            }
            writer = BufferedWriter(FileWriter(file))
        } catch (e: IOException) {
            Log.d(TAG, "File holding objects wasn't opened properly for write.")
        }

        gson.toJson(itemList, writer)
    }

    companion object {

        private val TAG = "File DataGateway"
    }

}
