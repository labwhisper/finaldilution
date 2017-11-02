package com.labessence.biotech.finaldilution.solution;

import com.labessence.biotech.finaldilution.component.Component;
import com.labessence.biotech.finaldilution.compound.Compound;
import com.labessence.biotech.finaldilution.genericitem.Item;

import java.util.ArrayList;
import java.util.List;

public class Solution implements Item {

    private String name;
    private double volume;

    private List<Component> components = new ArrayList<>();

    public Solution() {
    }

    public Solution(String name) {
        this.name = name;
    }

    public Solution(String name, double volume) {
        this.name = name;
        this.volume = volume;
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
        for (Component component : components) {
            component.setSolutionVolume(volume);
        }
    }

    public String calculateQuantities() {
        StringBuilder niceOutput = new StringBuilder(800);
        for (Component component : components) {
            niceOutput.append(component.getAmountString(getVolume()));
            niceOutput.append(System.getProperty("line.separator"));
        }
        return niceOutput.toString();
    }

    public boolean isOverflown() {
        return getAllLiquidComponentsVolume() > getVolume();
    }

    public double getAllLiquidComponentsVolume() {
        double allLiquidComponentsVolume = 0.0;
        for (Component component : components) {
            if (component.getFromStock()) {
                allLiquidComponentsVolume += component.getQuantity(getVolume());
            }
        }
        return allLiquidComponentsVolume;
    }


    public List<Component> getComponents() {
        return components;
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public Component getComponentWithCompound(Compound compound) {
        //TODO Change to stream when ready on Android
        for (Component component : components) {
            if (component.getCompound().equals(compound)) {
                return component;
            }
        }
        return null;
    }

    public void addComponent(Component component) {
        components.add(component);
    }
}
