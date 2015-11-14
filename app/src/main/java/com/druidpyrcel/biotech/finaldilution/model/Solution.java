package com.druidpyrcel.biotech.finaldilution.model;


import java.util.HashMap;
import java.util.Map;

public class Solution {
    private int id;
    private String name;
    private double volume;
    private Map<Compound, Double> componentList;

    public Solution() {
        componentList = new HashMap<Compound, Double>();
    }

    public Solution(String name, double volumeMili) {
        this.name = name;
        this.volume = volumeMili / 1000;
        componentList = new HashMap<Compound, Double>();
    }

    public boolean addComponent(Compound component, double molarConcentration) {
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

    public void changeConcentration(Compound component, double newMolarConcentration) {
        componentList.put(component, newMolarConcentration);
    }


    public String calculateQuantities() {
        StringBuilder niceOutput = new StringBuilder(200);
        for (Map.Entry<Compound, Double> compound : componentList.entrySet()) {
            double finalMass = compound.getKey().getMolarMass() * compound.getValue() * volume;
            niceOutput.append(compound.getKey().getShortName());
            niceOutput.append(" : ");
            if (finalMass > 1) {
                niceOutput.append(String.format("%1$,.3f", finalMass));
                niceOutput.append(" g");
            } else {
                niceOutput.append(String.format("%1$,.1f", finalMass * 1000));
                niceOutput.append(" mg");
            }
            niceOutput.append("\n");
        }
        return niceOutput.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getVolumeMili() {
        return volume * 1000;
    }

    public void setVolumeMili(double volumeMili) {
        this.volume = volumeMili / 1000;
    }

    public Map<Compound, Double> getComponentList() {
        return componentList;
    }

    public void setComponentList(Map<Compound, Double> componentList) {
        this.componentList = componentList;
    }

    @Override
    public String toString() {
        return name + ", " + getVolumeMili() + "ml, " + componentList.size() + " components";
    }
}
