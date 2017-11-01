package com.labessence.biotech.finaldilution;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.peripherals.DataGatewayOperations;
import com.labessence.biotech.finaldilution.solution.Solution;

public class ApplicationContext extends Application {
    private static final String TAG = "Application Context";
    public static final String FINAL_DILUTION_PREFERENCES = "FINAL_DILUTION_PREFERENCES";
    public static double SWIPE_MIN_VELOCITY = 100;
    public static double SWIPE_MIN_DISTANCE = 50;
    private static ApplicationContext instance;
    //TODO Remove currentSolution from here and pass it with Activities
    private Solution currentSolution;
    private DataGatewayOperations<Solution> solutionGateway;
    private DataGatewayOperations<Compound> compoundGateway;

    public ApplicationContext() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onTerminate() {
        //TODO Clear DataGateway
        super.onTerminate();
    }

    public Solution getCurrentSolution() {
        //TODO Current Solution bedzie przechowywane w podrecznych danych androida.
        //TODO Przeniesc mechanizm zapisywania stanu do nowego komponentu
        if (currentSolution == null) {
            SharedPreferences settings = getSharedPreferences(FINAL_DILUTION_PREFERENCES, 0);
            String storedSolutionName = settings.getString("currentSolution", null);
            if (storedSolutionName != null) {
                currentSolution = solutionGateway.load(storedSolutionName);
                Log.d(TAG, "Retrieved current solution using preferences : " + storedSolutionName);
            }
        }
        return currentSolution;
    }

    public void setCurrentSolution(Solution currentSolution) {
        this.currentSolution = currentSolution;
        SharedPreferences settings = getSharedPreferences(FINAL_DILUTION_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("currentSolution", currentSolution.getName());
        editor.apply();
        Log.d(TAG, "Stored current solution to preferences : " + currentSolution.getName());
    }

    public void setSolutionGateway(DataGatewayOperations<Solution> solutionGateway) {
        this.solutionGateway = solutionGateway;
    }

    public void setCompoundGateway(DataGatewayOperations<Compound> compoundGateway) {
        this.compoundGateway = compoundGateway;
    }

    public DataGatewayOperations<Solution> getSolutionGateway() {
        return solutionGateway;
    }

    public DataGatewayOperations<Compound> getCompoundGateway() {
        return compoundGateway;
    }
}
