package com.druidpyrcel.biotech.finaldilution;

import android.app.Application;

import com.druidpyrcel.biotech.finaldilution.model.Solution;
import com.druidpyrcel.biotech.finaldilution.sqlite.DataProvider;

public class ApplicationContext extends Application {
    private DataProvider db;
    private Solution currentSolution;


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
