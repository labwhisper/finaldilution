package com.druidpyrcel.biotech.finaldilution.model;


import java.util.HashMap;
import java.util.Map;

public class Solution {
    private String name;
    private double volume;
    private Map<String, Component> componentList;

    public Solution() {
        componentList = new HashMap<>();
    }

    public Solution(String name, double volumeMili) {
        this.name = name;
        this.volume = volumeMili / 1000;
        componentList = new HashMap<>();
    }

    public boolean addComponent(Component component) throws ItemExistsException {
        if (componentList.get(component.getCompound().getShortName()) != null) {
            return false;
        }
        for (Map.Entry<String, Component> componentEntry : componentList.entrySet()) {
            if (componentEntry.getKey().equals(component.getCompound().getShortName())) {
                throw new ItemExistsException();
            }
        }
        componentList.put(component.getCompound().getShortName(), component);
        return true;
    }

    public void removeComponent(Component component) {
        componentList.remove(component.getCompound().getShortName());
    }

    public void clearSolution() {
        componentList.clear();
    }

    public void changeConcentration(Component component, double newMolarConcentration) {
        componentList.put(component.getCompound().getShortName(), component);
    }


    public String calculateQuantities() {
        StringBuilder niceOutput = new StringBuilder(800);
        for (Map.Entry<String, Component> component : componentList.entrySet()) {
            niceOutput.append(component.getValue().getAmountString(volume));
            niceOutput.append(System.getProperty("line.separator"));
        }
        return niceOutput.toString();
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

    public Map<String, Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(Map<String, Component> componentList) {
        this.componentList = componentList;
    }

    @Override
    public String toString() {
        return name + ", " + getVolumeMili() + "ml, " + componentList.size() + " components";
    }
}
