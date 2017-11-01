package com.labessence.biotech.finaldilution.concentration;

import com.labessence.biotech.finaldilution.compound.Compound;

import java.io.Serializable;

public class Component implements Serializable {

    private boolean fromStock;

    private Concentration desiredConcentration;
    private Concentration availableConcentration;
    private Compound compound;
    private double solutionVolume;

    //TODO Remove empty constructor without solution - used only in tests
    Component() {
    }

    public Component(boolean fromStock, double solutionVolume, Compound compound) {
        this.fromStock = fromStock;
        this.solutionVolume = solutionVolume;
        this.compound = compound;
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
            niceOutput.append(String.format("%1$,.3f", amount));
            if (getFromStock()) {
                niceOutput.append(" ml");
            } else {
                niceOutput.append(" g");
            }
        } else {
            niceOutput.append(String.format("%1$,.1f", amount * 1000));
            if (getFromStock()) {
                niceOutput.append(" ul");
            } else {
                niceOutput.append(" mg");
            }
        }
        return niceOutput.toString();
    }

    public double getQuantity(double volume) {
        if (getFromStock()) {
            return calcVolumeForDesiredMass(calcDesiredMass(volume));
        } else {
            return calcDesiredMass(volume);
        }
    }

    /**
     * Calculate desired mass depending on desired concentration
     *
     * @param volume - volume[ml] of the final solution
     * @return - desired mass[g]
     */
    private double calcDesiredMass(double volume) {

        if (getCompound() == null || getDesiredConcentration() == null) {
            return 0;
        }

        //TODO replace case with inheritance
        double c = getDesiredConcentration().getAmount();
        double M = getCompound().getMolarMass();
        switch (getDesiredConcentration().getType()) {
            case PERCENTAGE:
                return c * volume / 100;
            case MOLAR:
            default:
                return c * volume * M / 1000;
            case MILIMOLAR:
                return c * volume * M / 1000 / 1000;
            case MILIGRAM_PER_MILLILITER:
                return c * volume / 1000;
        }
    }

    /**
     * Calculate component volume using calculated desired mass
     *
     * @param mass - mass[g] required in final solution
     * @return - volume[ml] of compound to be taken
     */
    private double calcVolumeForDesiredMass(double mass) {

        if (getCompound() == null || getDesiredConcentration() == null) {
            return 0;
        }

        double c = getAvailableConcentration().getAmount();
        double M = getCompound().getMolarMass();
        switch (getAvailableConcentration().getType()) {
            case PERCENTAGE:
                return mass / c * 100;
            case MOLAR:
            default:
                return mass / M / c * 1000;
            case MILIMOLAR:
                return mass / M / c * 1000 * 1000;
            case MILIGRAM_PER_MILLILITER:
                return mass / c * 1000;
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
