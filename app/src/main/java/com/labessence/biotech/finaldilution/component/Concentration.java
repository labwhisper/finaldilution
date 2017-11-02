package com.labessence.biotech.finaldilution.component;

public class Concentration {

    private double amount;
    private ConcentrationType type;

    public Concentration() {
    }

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

    public void setType(ConcentrationType type) {
        this.type = type;
    }

    @Override
    public String toString() {

        return getAmount() + " [" + type.toString() + "]";
    }

}
