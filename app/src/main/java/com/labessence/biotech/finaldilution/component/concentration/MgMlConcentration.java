package com.labessence.biotech.finaldilution.component.concentration;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/11/2017.
 */

public class MgMlConcentration extends Concentration {
    public MgMlConcentration(double amount) {
        super(amount, ConcentrationType.MILIGRAM_PER_MILLILITER);
    }

    @Override
    public double calcDesiredMass(double volume, double molarMass) {
        return concentration * volume / 1000;
    }

    @Override
    public double calcVolumeForDesiredMass(double mass, double molarMass) {
        return mass / concentration * 1000;
    }
}
