package com.druidpyrcel.biotech.finaldilution.model;

/**
 * Solution Component
 */
public class Component {

    private Compound compound;
    private boolean fromStock;
    private Concentration desiredConcentration;
    private Concentration ownedConcentration = null;
    private double quantity;
    //TODO addedToSolution should be in different list
    private boolean addedToSolution;

    /**
     * Generate component using compound - default
     *
     * @param compound
     * @param desiredConcentration
     */
    public Component(Compound compound, Concentration desiredConcentration) {
        this.compound = compound;
        this.desiredConcentration = desiredConcentration;
        this.fromStock = false;
    }

    /**
     * Generate component using prepared stack concentration
     *
     * @param compound
     * @param desiredConcentration
     * @param ownedConcentration
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
            return calcAmoutForDesiredMass(calcDesiredMass(volume));
        } else {
            return calcDesiredMass(volume);
        }
    }

    /**
     * Calculate desired mass depending on desired concentration
     *
     * @param volume
     * @return
     */
    private double calcDesiredMass(double volume) {

        double c = desiredConcentration.getAmount();
        double v = volume;
        double M = compound.getMolarMass();
        switch (desiredConcentration.getType()) {
            case PERCENTAGE:
                return c * v * 10;
            case MOLAR:
            default:
                return c * v * M;
            case MILIMOLAR:
                return c * v * M / 1000;
            case MILIGRAM_PER_MILILITER:
                return c * v * 1000;
        }
    }

    /**
     * Calculate final amount using calculated desired mass
     *
     * @param desiredMass
     * @return
     */
    private double calcAmoutForDesiredMass(double desiredMass) {

        double c = ownedConcentration.getAmount();
        double m = desiredMass;
        double M = compound.getMolarMass();
        switch (ownedConcentration.getType()) {
            case PERCENTAGE:
                return m / c * 100;
            case MOLAR:
            default:
                return m / M / c;
            case MILIMOLAR:
                return m / M / c * 1000;
            case MILIGRAM_PER_MILILITER:
                return m / c;
        }
    }

    public Compound getCompound() {
        return compound;
    }

}
