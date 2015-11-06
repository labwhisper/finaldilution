package com.druidpyrcel.biotech.finaldilution.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "FinalDilutionDB.sqlite";
    private static final int DATABASE_VERSION = 1;
    // Compounds Table
    private static final String TABLE_COMPOUNDS = "compounds";
    private static final String COMPOUNDS_KEY_ID = "_id";
    private static final String COMPOUNDS_KEY_SHORT_NAME = "shortName";
    private static final String COMPOUNDS_KEY_MOLAR_MASS = "molarMass";
    private static final String[] COMPOUNDS_COLUMNS = {COMPOUNDS_KEY_ID, COMPOUNDS_KEY_SHORT_NAME, COMPOUNDS_KEY_MOLAR_MASS};
    // Solutions Table
    private static final String TABLE_SOLUTIONS = "solutions";
    private static final String SOLUTIONS_KEY_ID = "_id";
    private static final String SOLUTIONS_KEY_NAME = "name";
    private static final String SOLUTIONS_KEY_VOLUME = "volume";
    // Assignments Table
    private static final String TABLE_ASSIGNMENTS = "compounds_assignments";
    private static final String ASSIGNMENTS_KEY_COMPOUND = "compound";
    private static final String ASSIGNMENTS_KEY_SOLUTION = "solution";
    private static final String ASSIGNMENTS_KEY_QUANTITY = "quantity";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older compounds table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPOUNDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLUTIONS);
        //TODO create fresh tables
    }

    public void addSolution(Solution solution) {
        Log.d("addSolution", solution.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_SOLUTIONS +
                " WHERE " + SOLUTIONS_KEY_NAME + "=" + "'" + solution.getName() + "'";
        db.execSQL(query);
        db.close();

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SOLUTIONS_KEY_NAME, solution.getName());
        values.put(SOLUTIONS_KEY_VOLUME, solution.getVolume());
        db.insert(TABLE_SOLUTIONS, null, values);
        db.close();
    }

    public List<Solution> getAllSolutions() {
        List<Solution> solutions = new LinkedList<Solution>();

        String query = "SELECT * FROM " + TABLE_COMPOUNDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Solution solution = null;
        if (cursor.moveToFirst()) {
            do {
                solution = new Solution();
                solution.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOLUTIONS_KEY_ID))));
                solution.setName(cursor.getString(cursor.getColumnIndex(SOLUTIONS_KEY_NAME)));
                solution.setVolume(cursor.getDouble(cursor.getColumnIndex(SOLUTIONS_KEY_VOLUME)));
                solutions.add(solution);
            } while (cursor.moveToNext());
        }
        db.close();

        Log.d("getAllSolutions()", solutions.toString());
        return solutions;
    }

    public Solution getSolution(String name) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SOLUTIONS +
                " WHERE " + SOLUTIONS_KEY_NAME + "=" + "'" + name + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null || !cursor.moveToFirst()) {
            db.close();
            return null;
        }

        Solution solution = new Solution();
        solution.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOLUTIONS_KEY_ID))));
        solution.setName(cursor.getString(cursor.getColumnIndex(SOLUTIONS_KEY_NAME)));
        solution.setVolume(cursor.getDouble(cursor.getColumnIndex(SOLUTIONS_KEY_VOLUME)));

        query = "SELECT * " +
                "FROM " + TABLE_ASSIGNMENTS + ", " + TABLE_COMPOUNDS + ", " + TABLE_SOLUTIONS +
                " WHERE " + TABLE_SOLUTIONS + "." + SOLUTIONS_KEY_NAME + "=" + "'" + name + "'" +
                " AND " + TABLE_SOLUTIONS + "." + SOLUTIONS_KEY_ID + " = " +
                TABLE_ASSIGNMENTS + "." + ASSIGNMENTS_KEY_SOLUTION +
                " AND " + TABLE_COMPOUNDS + "." + COMPOUNDS_KEY_ID + " = " +
                TABLE_ASSIGNMENTS + "." + ASSIGNMENTS_KEY_COMPOUND;
        cursor = db.rawQuery(query, null);

        if (cursor == null || !cursor.moveToFirst()) {
            db.close();
            return solution;
        }

        Compound compound = null;
        do {
            compound = new Compound();
            compound.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_ID))));
            compound.setShortName(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_SHORT_NAME)));
            compound.setMolarMass(cursor.getDouble(cursor.getColumnIndex(COMPOUNDS_KEY_MOLAR_MASS)));
            double quantity = cursor.getDouble(cursor.getColumnIndex(ASSIGNMENTS_KEY_QUANTITY));
            solution.addComponent(compound, quantity);
        } while (cursor.moveToNext());
        db.close();

        Log.d("getSolution(" + name + ")", solution.toString());
        return solution;
    }

    public void addCompound(Compound compound) {
        Log.d("addCompound", compound.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COMPOUNDS_KEY_SHORT_NAME, compound.getShortName());
        values.put(COMPOUNDS_KEY_MOLAR_MASS, compound.getMolarMass());

        db.insert(TABLE_COMPOUNDS, null, values);
        db.close();
    }

    public Compound getCompound(String shortName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_COMPOUNDS, // a. table
                        COMPOUNDS_COLUMNS, // b. column names
                        " " + COMPOUNDS_KEY_SHORT_NAME + " = ?", // c. selections
                        new String[]{String.valueOf(shortName)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor == null)
            return null;

        cursor.moveToFirst();

        Compound compound = new Compound();
        compound.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_ID))));
        compound.setShortName(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_SHORT_NAME)));
        compound.setMolarMass(cursor.getDouble(cursor.getColumnIndex(COMPOUNDS_KEY_MOLAR_MASS)));
        db.close();

        Log.d("getCompound(" + shortName + ")", compound.toString());
        return compound;
    }

    public List<Compound> getAllCompounds() {
        List<Compound> compounds = new LinkedList<Compound>();

        String query = "SELECT  * FROM " + TABLE_COMPOUNDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Compound compound = null;
        if (cursor.moveToFirst()) {
            do {
                compound = new Compound();
                compound.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_ID))));
                compound.setShortName(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_SHORT_NAME)));
                compound.setMolarMass(cursor.getDouble(cursor.getColumnIndex(COMPOUNDS_KEY_MOLAR_MASS)));
                compounds.add(compound);
            } while (cursor.moveToNext());
        }
        db.close();

        Log.d("getAllCompounds()", compounds.toString());
        return compounds;
    }


}
