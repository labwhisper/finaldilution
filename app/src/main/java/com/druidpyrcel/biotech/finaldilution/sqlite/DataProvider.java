package com.druidpyrcel.biotech.finaldilution.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataProvider extends SQLiteAssetHelper {

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

    public DataProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older compounds table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPOUNDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLUTIONS);
        //TODO create fresh tables
    }

    public void deleteSolution(Solution solution) {
        Log.d("addSolution", solution.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_SOLUTIONS +
                " WHERE " + SOLUTIONS_KEY_NAME + "=" + "'" + solution.getName() + "'";
        db.execSQL(query);
        db.close();
    }

    public void addSolution(Solution solution) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SOLUTIONS_KEY_NAME, solution.getName());
        values.put(SOLUTIONS_KEY_VOLUME, solution.getVolume());
        db.insert(TABLE_SOLUTIONS, null, values);

        //add Components and assignments
        for (Map.Entry<Compound, Double> compound : solution.getComponentList().entrySet()) {
            ContentValues assignmentsValues = new ContentValues();
            assignmentsValues.put(ASSIGNMENTS_KEY_COMPOUND, compound.getKey().getId());
            assignmentsValues.put(ASSIGNMENTS_KEY_QUANTITY, compound.getValue());
            db.insert(TABLE_ASSIGNMENTS, null, assignmentsValues);
        }
        db.close();
    }

    public void updateSolution(Solution solution) {
        deleteSolution(solution);
        addSolution(solution);
    }

    public List<Solution> getAllSolutions() {
        List<Solution> solutions = new LinkedList<>();

        String query = "SELECT * FROM " + TABLE_SOLUTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Solution solution = fillSolution(db, cursor);
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

        Solution solution = fillSolution(db, cursor);

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

        Compound compound = fillCompound(cursor);
        db.close();

        Log.d("getCompound(" + shortName + ")", compound.toString());
        return compound;
    }

    public List<Compound> getAllCompounds() {
        List<Compound> compounds = new LinkedList<>();

        String query = "SELECT  * FROM " + TABLE_COMPOUNDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Compound compound = fillCompound(cursor);
                compounds.add(compound);
            } while (cursor.moveToNext());
        }
        db.close();

        Log.d("getAllCompounds()", compounds.toString());
        return compounds;
    }


    @NonNull
    private Cursor selectAllSolutionAssociations(SQLiteDatabase db, String name) {
        String query;
        query = "SELECT * " +
                "FROM " + TABLE_ASSIGNMENTS + ", " + TABLE_COMPOUNDS + ", " + TABLE_SOLUTIONS +
                " WHERE " + TABLE_SOLUTIONS + "." + SOLUTIONS_KEY_NAME + "=" + "'" + name + "'" +
                " AND " + TABLE_SOLUTIONS + "." + SOLUTIONS_KEY_ID + " = " +
                TABLE_ASSIGNMENTS + "." + ASSIGNMENTS_KEY_SOLUTION +
                " AND " + TABLE_COMPOUNDS + "." + COMPOUNDS_KEY_ID + " = " +
                TABLE_ASSIGNMENTS + "." + ASSIGNMENTS_KEY_COMPOUND;
        return db.rawQuery(query, null);
    }

    @NonNull
    private Compound fillCompound(Cursor cursor) {
        Compound compound = new Compound();
        compound.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_ID))));
        compound.setShortName(cursor.getString(cursor.getColumnIndex(COMPOUNDS_KEY_SHORT_NAME)));
        compound.setMolarMass(cursor.getDouble(cursor.getColumnIndex(COMPOUNDS_KEY_MOLAR_MASS)));
        return compound;
    }


    @NonNull
    private Solution fillSolution(SQLiteDatabase db, Cursor cursor) {
        Solution solution = new Solution();
        solution.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOLUTIONS_KEY_ID))));
        String name = cursor.getString(cursor.getColumnIndex(SOLUTIONS_KEY_NAME));
        solution.setName(name);
        solution.setVolume(cursor.getDouble(cursor.getColumnIndex(SOLUTIONS_KEY_VOLUME)));

        cursor = selectAllSolutionAssociations(db, name);

        if (cursor == null || !cursor.moveToFirst()) {
            return solution;
        }

        do {
            Compound compound = fillCompound(cursor);
            double quantity = cursor.getDouble(cursor.getColumnIndex(ASSIGNMENTS_KEY_QUANTITY));
            solution.addComponent(compound, quantity);
        } while (cursor.moveToNext());

        return solution;
    }

}
