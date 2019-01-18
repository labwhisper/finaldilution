package com.labessence.biotech.finaldilution.peripherals.datastores

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.labessence.biotech.finaldilution.component.concentration.Concentration
import com.labessence.biotech.finaldilution.genericitem.Item
import com.labessence.biotech.finaldilution.peripherals.DataGatewayOperations
import java.util.*

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 4/3/2017.
 */

//TODO gson.toJsonTree(compound);
// if found - load, if not create.
// not found : no solutions.
// solution created - saved.

//private static final File COMPOUNDS_FILE = new File("compounds.json");
class SharedPreferencesStore<T : Item>(
    private val sharedPreferences: SharedPreferences, private val listType: TypeToken<List<T>>
) : DataGatewayOperations<T> {

    private val gson = GsonBuilder()
        .registerTypeAdapter(Concentration::class.java, ConcentrationDeserializer())
        .create()

    private val itemListFormPreferences: MutableList<T>
        get() {

            val rootJson = sharedPreferences.getString("root", "")
            val loadedList = gson.fromJson<List<T>>(rootJson, listType.type)

            if (loadedList == null) {
                Log.d(TAG, "Loading from preferences failed.")
                return ArrayList()
            }
            return loadedList as MutableList<T>
        }

    override fun size() = itemListFormPreferences.size


    override fun save(item: T) {
        //TODO performance
        // Could use append
        // http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java

        val itemList = itemListFormPreferences
        if (itemList.contains(item)) {
            Log.d(TAG, "Item " + item.name + " already present.")
            return
        }
        itemList.add(item)
        putItemListToPreferences(itemList)
        Log.d(TAG, "Item " + item.name + " added.")
    }


    override fun rename(item: T, oldName: String) {
        val itemList = itemListFormPreferences
        if (itemList.isEmpty()) {
            return
        }

        removeItemByName(itemList, oldName)

        itemList.add(item)
        putItemListToPreferences(itemList)
        Log.d(TAG, "Item " + oldName + " renamed to " + item.name)
    }

    override fun update(item: T) {
        val itemList = itemListFormPreferences
        //TODO Add means to update
        if (itemList.isEmpty()) {
            return
        }

        removeItemByName(itemList, item.name)

        itemList.add(item)
        putItemListToPreferences(itemList)
        Log.d(TAG, "Item " + item.name + " updated.")
    }

    private fun removeItemByName(itemList: MutableList<T>, name: String) {
        val i = itemList.iterator()
        while (i.hasNext()) {
            val currentItem = i.next()
            if (currentItem.name == name) {
                i.remove()
            }
        }
    }

    override fun remove(item: T) {
        val itemList = itemListFormPreferences
        if (itemList.isEmpty()) {
            return
        }
        removeItemByName(itemList, item.name)
        putItemListToPreferences(itemList)
        Log.d(TAG, "Item " + item.name + " removed.")
    }

    override fun load(name: String): T? {
        val itemList = itemListFormPreferences
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
        return itemListFormPreferences
    }

    private fun putItemListToPreferences(itemList: List<T>) {
        val prefsEditor = sharedPreferences.edit()
        val rootJson = gson.toJson(itemList)
        prefsEditor.putString("root", rootJson)
        prefsEditor.apply()
    }

    companion object {

        private val TAG = "File DataGateway"
    }

}
