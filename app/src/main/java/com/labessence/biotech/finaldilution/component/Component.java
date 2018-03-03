package com.labessence.biotech.finaldilution.component;

import com.labessence.biotech.finaldilution.compound.Compound;

import java.io.Serializable;
import java.util.Locale;

public class Component implements Serializable {

    private boolean fromStock;

    private Concentration desiredConcentration;
    private Concentration availableConcentration;
    private Compound compound;
    private double solutionVolume = 0;

    public Component(double solutionVolume, Compound compound) {
        this.solutionVolume = solutionVolume;
        this.compound = compound;
    }

    Component(Compound compound, Concentration desired, Concentration stock) {
        this.compound = compound;
        desiredConcentration = desired;
        fromStock = true;
        availableConcentration = stock;
    }

    Component(Compound compound, Concentration desired) {
        this.compound = compound;
        desiredConcentration = desired;
        fromStock = false;
    }

    public boolean getFromStock() {
        return fromStock;
    }

    public void setFromStock(boolean fromStock) {
        this.fromStock = fromStock;
    }

    @Override
    public String toString() {
        if (getCompound() == null) {
            return "";
        }
        return getCompound().getShortName() + " : " + getAmountString(solutionVolume);
    }

    public String getAmountString(double volume) {

        double amount = getQuantity(volume);
        StringBuilder niceOutput = new StringBuilder(200);
        if (amount > 1) {
            niceOutput.append(String.format(Locale.ENGLISH, "%1$,.3f", amount));
            if (getFromStock()) {
                niceOutput.append(" ml");
            } else {
                niceOutput.append(" g");
            }
        } else {
            niceOutput.append(String.format(Locale.ENGLISH, "%1$,.1f", amount * 1000));
            if (getFromStock()) {
                niceOutput.append(" ul");
            } else {
                niceOutput.append(" mg");
            }
        }
        return niceOutput.toString();
    }

    public double getQuantity(double volume) {
        double M = getCompound().getMolarMass();
        if (getFromStock()) {
            return availableConcentration.calcVolumeForDesiredMass(desiredConcentration
                    .calcDesiredMass(volume, M), M);
        } else {
            return desiredConcentration.calcDesiredMass(volume, M);
        }
    }

    public Compound getCompound() {
        return compound;
    }

    public void setCompound(Compound compound) {
        this.compound = compound;
    }

    public Concentration getDesiredConcentration() {
        return desiredConcentration;
    }

    public void setDesiredConcentration(Concentration desiredConcentration) {
        this.desiredConcentration = desiredConcentration;
    }

    public Concentration getAvailableConcentration() {
        return availableConcentration;
    }

    public void setAvailableConcentration(Concentration availableConcentration) {
        this.availableConcentration = availableConcentration;
    }

    public double getSolutionVolume() {
        return solutionVolume;
    }

    public void setSolutionVolume(double solutionVolume) {
        this.solutionVolume = solutionVolume;
    }
}
