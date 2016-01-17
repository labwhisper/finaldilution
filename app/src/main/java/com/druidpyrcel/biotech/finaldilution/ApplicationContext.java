package com.druidpyrcel.biotech.finaldilution;

import android.app.Application;
import android.content.Context;

import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.druidpyrcel.biotech.finaldilution.sqlite.DataProvider;

public class ApplicationContext extends Application {
    static double SWIPE_MIN_VELOCITY = 200;
    static double SWIPE_MIN_DISTANCE = 100;
    private static ApplicationContext instance;
    private DataProvider db;
    private Solution currentSolution;

    public ApplicationContext(){
        instance = this;
    }

    public static Context getContext(){
        return instance;
    }

    public DataProvider getDb() {
        return db;
    }

    public void setDb(DataProvider db) {
        this.db = db;
    }

    public Solution getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(Solution currentSolution) {
        this.currentSolution = currentSolution;
    }
}
