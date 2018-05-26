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
//        this.compoundGateway.save(new Compound("KCl", 74.55));
//        this.compoundGateway.save(new Compound("NaCl", 58.44));
//        this.compoundGateway.save(new Compound("EDTA", 372.24));
//        this.compoundGateway.save(new Compound("MgCl2", 95.21));
//        this.compoundGateway.save(new Compound("TRIS", 121.14));
//        this.compoundGateway.save(new Compound("SDS", 288.372));
    }

    public DataGatewayOperations<Solution> getSolutionGateway() {
        return solutionGateway;
    }

    public DataGatewayOperations<Compound> getCompoundGateway() {
        return compoundGateway;
    }

    public void removeCompoundFromEverywhere(Compound compound) {
        saveCurrentWorkOnSolution();
        for (Solution solution : getSolutionGateway().loadAll()) {
            if (solution.getComponentWithCompound(compound) != null) {
                solution.removeComponent(solution.getComponentWithCompound(compound));
                getSolutionGateway().update(solution);
            }
        }
        if (getCurrentSolution().getComponentWithCompound(compound) != null) {
            getCurrentSolution().removeComponent(getCurrentSolution().getComponentWithCompound(compound));
        }
        getCompoundGateway().remove(compound);
    }

    public void saveCurrentWorkOnSolution() {
        getSolutionGateway().update(getCurrentSolution());
    }
}
