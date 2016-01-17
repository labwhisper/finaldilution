package com.druidpyrcel.biotech.finaldilution.model;

public class Concentration {

    public enum ConcentrationType {
        PERCENTAGE,
        MOLAR,
        MILIMOLAR,
        MILIGRAM_PER_MILILITER;
    }

    private double amount;
    private ConcentrationType type;

    public Concentration(double amount, ConcentrationType type) {
        this.amount = amount;
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ConcentrationType getType() {
        return type;
    }

    public enum ConcentrationType {
        PERCENTAGE,
        MOLAR,
        MILIMOLAR,
        MILIGRAM_PER_MILILITER;
    }

    public void setType(ConcentrationType type) {
        this.type = type;
    }
}
