package com.druidpyrcel.biotech.finaldilution.model;

/**
 * Solution Component
 */
public class Component {

    private Compound compound;
    private boolean fromStack;
    private Concentration desiredConcentration;
    private Concentration ownedConcentration;
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
        this.fromStack = false;
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
        this.fromStack = true;
    }

    public String getAmountString(double volume) {
        //TODO Add ownedConc + fromStack somehow
        switch (desiredConcentration.getType()) {
            case PERCENTAGE:
                return getAmountForConc(volume);
            case MOLAR:
            default:
                return getAmountForMolarConc(volume);
            case MILIMOLAR:
                return "Not implemented";
            case MILIGRAM_PER_MILILITER:
                return "Not implemented";
        }
//        if( fromStack ) {
//            return getAmountStringStack(volume);
//        } else {
//            return getAmountStringCompound(volume);
//        }
    }

    public String getAmountForConc(double volume) {
        return "Not implemented";
    }

    public String getAmountForMolarConc(double volume) {

        StringBuilder niceOutput = new StringBuilder(200);
        double finalMass = compound.getMolarMass() * desiredConcentration.getAmount() * volume;
        niceOutput.append(compound.getShortName());
        niceOutput.append(" : ");
        if (finalMass > 1) {
            niceOutput.append(String.format("%1$,.3f", finalMass));
            niceOutput.append(" g");
        } else {
            niceOutput.append(String.format("%1$,.1f", finalMass * 1000));
            niceOutput.append(" mg");
        }
        return niceOutput.toString();
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Compound getCompound() {
        return compound;
    }

}
// Jak zrealizować, skoro:
// Głównie używamy proszku, dodatkowo może być from stack.
// Czyli mamy Component.
//                boolean fromStack -- (g/ml, ownedConc)
//                Concentration desiredConcentration
//                Concentration ownedConcentration (only from stack)
//                double calculateQuantity() (in g/ml  noStack/stack)
//                double quantity
//                String getQuantityString()

//                Interface Concentration ( %, M, mg/ml, mM )
