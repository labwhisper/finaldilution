package com.labessence.biotech.finaldilution.component;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/11/2017.
 */

class PercentageConcentration extends Concentration {
    public PercentageConcentration(double amount) {
        super(amount, ConcentrationType.PERCENTAGE);
    }

    @Override
    double calcDesiredMass(double volume, double molarMass) {
        return concentration * volume / 100;
    }

    @Override
    double calcVolumeForDesiredMass(double mass, double molarMass) {
        return mass / concentration * 100;
    }
}
