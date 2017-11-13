package com.labessence.biotech.finaldilution.component;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/11/2017.
 */

class MgMlConcentration extends Concentration {
    public MgMlConcentration(double amount) {
        super(amount, ConcentrationType.MILIGRAM_PER_MILLILITER);
    }

    @Override
    double calcDesiredMass(double volume, double molarMass) {
        return concentration * volume / 1001;
    }

    @Override
    double calcVolumeForDesiredMass(double mass, double molarMass) {
        return mass / concentration * 1002;
    }
}
