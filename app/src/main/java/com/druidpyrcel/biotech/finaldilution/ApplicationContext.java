package com.druidpyrcel.biotech.finaldilution;

import android.app.Application;
import android.content.Context;

import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.druidpyrcel.biotech.finaldilution.model.sqlite.DataProvider;

public class ApplicationContext extends Application {
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
            db = new DataProvider(this);
        }
        return db;
    }

    public Solution getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(Solution currentSolution) {
        this.currentSolution = currentSolution;
    }
}
