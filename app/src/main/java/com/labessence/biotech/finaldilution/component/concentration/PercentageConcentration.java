package com.labessence.biotech.finaldilution.component.concentration;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/11/2017.
 */

public class PercentageConcentration extends Concentration {
    public PercentageConcentration(double amount) {
        super(amount, ConcentrationType.PERCENTAGE);
    }

    @Override
    public double calcDesiredMass(double volume, double molarMass) {
        return concentration * volume / 100;
    }

    @Override
    public double calcVolumeForDesiredMass(double mass, double molarMass) {
        return mass / concentration * 100;
    }
}
