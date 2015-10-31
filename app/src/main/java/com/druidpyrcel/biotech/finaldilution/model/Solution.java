package com.druidpyrcel.biotech.finaldilution.model;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by dawid.chmielewski on 10/11/2015.
 */
public class Solution {
    private double volumeMili;
    private Map<Compound, Float> componentList;

    public Solution(double volumeMili) {
        this.volumeMili = volumeMili;
        componentList = new HashMap<Compound, Float>();
    }

    public boolean addComponent(Compound component, float molarConcentration) {
        if (componentList.get(component) != null) {
            return false;
        }
        componentList.put(component, molarConcentration);
        return true;
    }

    public void removeComponent(Compound component) {
        componentList.remove(component);
    }

    public void clearSolution() {
        componentList.clear();
    }

    public void changeConcentration(Compound component, float newMolarConcentration) {
        componentList.put(component, newMolarConcentration);
    }


    public String calculateQuantities() {
        StringBuilder niceOutput = new StringBuilder(200);
        for (Map.Entry<Compound, Float> compound : componentList.entrySet()) {
            double finalMassMili = compound.getKey().getMolarMass() * compound.getValue() * volumeMili;
            niceOutput.append(compound.getKey().getShortName());
            niceOutput.append(" : ");
            if (finalMassMili > 1000) {
                niceOutput.append(String.format("%1$,.3f", finalMassMili / 1000));
                niceOutput.append(" g");
            } else {

                niceOutput.append(String.format("%1$,.1f", finalMassMili));
                niceOutput.append(" mg");
            }
            niceOutput.append("\n");
        }
        return niceOutput.toString();
    }

    public double getVolumeMili() {
        return volumeMili;
    }

    public void setVolumeMili(double volumeMili) {
        this.volumeMili = volumeMili;
    }
}
