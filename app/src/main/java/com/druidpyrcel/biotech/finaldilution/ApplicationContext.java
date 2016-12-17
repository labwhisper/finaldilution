package com.druidpyrcel.biotech.finaldilution;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.druidpyrcel.biotech.finaldilution.model.sqlite.DataProvider;

public class ApplicationContext extends Application {
    private static final String TAG = "Application Context";
    public static final String FINAL_DILUTION_PREFERENCES = "FINAL_DILUTION_PREFERENCES";
    public static double SWIPE_MIN_VELOCITY = 100;
    public static double SWIPE_MIN_DISTANCE = 50;
    private static ApplicationContext instance;
    private DataProvider db;
    private Solution currentSolution;

    public ApplicationContext() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    public DataProvider getDb() {
        if (db == null) {
            Log.d(TAG, "Creating DataProvider");
            db = new DataProvider(this);
        }
        return db;
    }

    @Override
    public void onTerminate() {
        clearDb();
        super.onTerminate();
    }

    public void clearDb() {
        if (db == null) {
            return;
        }
        Log.d(TAG, "Removing DataProvider");
        db.closeDbConnections();
        db = null;
    }

    public Solution getCurrentSolution() {
        if (currentSolution == null) {
            SharedPreferences settings = getSharedPreferences(FINAL_DILUTION_PREFERENCES, 0);
            String storedSolutionName = settings.getString("currentSolution", null);
            if (storedSolutionName != null) {
                currentSolution = getDb().getSolution(storedSolutionName);
                Log.d(TAG, "Retrieved current solution from preferences : " + storedSolutionName);
            }
        }
        return currentSolution;
    }

    public void setCurrentSolution(Solution currentSolution) {
        this.currentSolution = currentSolution;
        SharedPreferences settings = getSharedPreferences(FINAL_DILUTION_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("currentSolution", currentSolution.getName());
        Log.d(TAG, "Stored current solution to preferences : " + currentSolution.getName());
    }
}
