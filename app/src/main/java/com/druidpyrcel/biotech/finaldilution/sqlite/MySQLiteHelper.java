package com.druidpyrcel.biotech.finaldilution.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "CompoundDB.sqlite";
    private static final int DATABASE_VERSION = 1;
    // Compounds Table Columns names
    private static final String TABLE_COMPOUNDS = "compounds";
    private static final String KEY_ID = "id";
    private static final String KEY_SHORT_NAME = "shortName";
    private static final String KEY_MOLAR_MASS = "molarMass";
    private static final String[] COLUMNS = {KEY_ID, KEY_SHORT_NAME, KEY_MOLAR_MASS};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older compounds table if existed
        db.execSQL("DROP TABLE IF EXISTS compounds");

        // create fresh compounds table
        this.onCreate(db);
    }

    public void addCompound(Compound compound) {
        Log.d("addCompound", compound.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SHORT_NAME, compound.getShortName());
        values.put(KEY_MOLAR_MASS, compound.getMolarMass());

        db.insert(TABLE_COMPOUNDS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public Compound getCompound(String shortName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_COMPOUNDS, // a. table
                        COLUMNS, // b. column names
                        " " + KEY_SHORT_NAME + " = ?", // c. selections
                        new String[]{String.valueOf(shortName)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor == null)
            return null;

        cursor.moveToFirst();

        Compound compound = new Compound();
        compound.setId(Integer.parseInt(cursor.getString(0)));
        compound.setShortName(cursor.getString(1));
        compound.setMolarMass(cursor.getFloat(2));
        db.close();

        Log.d("getCompound(" + shortName + ")", compound.toString());
        return compound;
    }

    public List<Compound> getAllCompounds() {
        List<Compound> compounds = new LinkedList<Compound>();

        String query = "SELECT  * FROM " + TABLE_COMPOUNDS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Compound compound = null;
        if (cursor.moveToFirst()) {
            do {
                compound = new Compound();
                compound.setId(Integer.parseInt(cursor.getString(0)));
                compound.setShortName(cursor.getString(1));
                compound.setMolarMass(cursor.getFloat(2));
                compounds.add(compound);
            } while (cursor.moveToNext());
        }
        db.close();

        Log.d("getAllCompounds()", compounds.toString());
        return compounds;
    }


}
