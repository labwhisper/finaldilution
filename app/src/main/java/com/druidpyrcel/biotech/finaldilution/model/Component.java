package com.druidpyrcel.biotech.finaldilution.model;

/**
 * Solution Component
 */
public class Component {

    private Compound compound;
    private boolean fromStock;
    private Concentration desiredConcentration;
    private Concentration ownedConcentration = null;
    //TODO addedToSolution should be in different list
    private boolean addedToSolution;

    /**
     * Generate component using compound - default
     *
     * @param compound - compound to be added to solution
     * @param desiredConcentration - concentration required in final solution
     */
    public Component(Compound compound, Concentration desiredConcentration) {
        this.compound = compound;
        this.desiredConcentration = desiredConcentration;
        this.fromStock = false;
    }

    /**
     * Generate component using prepared stack concentration
     *
     * @param compound - compound to be added to solution
     * @param desiredConcentration - concentration required in final solution
     * @param ownedConcentration - concentration available in lab
     */
    public Component(Compound compound, Concentration desiredConcentration, Concentration ownedConcentration) {
        this.compound = compound;
        this.desiredConcentration = desiredConcentration;
        this.ownedConcentration = ownedConcentration;
        this.fromStock = true;
    }

    public String getAmountString(double volume) {

        double amount = getQuantity(volume);
        StringBuilder niceOutput = new StringBuilder(200);
        niceOutput.append(compound.getShortName());
        niceOutput.append(" : ");
        if (amount > 1) {
            niceOutput.append(String.format("%1$,.3f", amount));
            if (fromStock) {
                niceOutput.append(" ml");
            } else {
                niceOutput.append(" g");
            }
        } else {
            niceOutput.append(String.format("%1$,.1f", amount * 1000));
            if (fromStock) {
                niceOutput.append(" ul");
            } else {
                niceOutput.append(" mg");
            }
        }
        return niceOutput.toString();
    }

    public double getQuantity(double volume) {
        if (fromStock) {
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

        double c = desiredConcentration.getAmount();
        double M = compound.getMolarMass();
        switch (desiredConcentration.getType()) {
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

        double c = ownedConcentration.getAmount();
        double M = compound.getMolarMass();
        switch (ownedConcentration.getType()) {
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

}
