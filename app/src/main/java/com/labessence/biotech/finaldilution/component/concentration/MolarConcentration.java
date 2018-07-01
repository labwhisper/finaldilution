package com.labessence.biotech.finaldilution.component.concentration;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/11/2017.
 */

public class MolarConcentration extends Concentration {
    public MolarConcentration(double amount) {
        super(amount, ConcentrationType.MOLAR);
    }

    @Override
    public double calcDesiredMass(double volume, double molarMass) {
        return concentration * volume * molarMass / 1000;
    }

    @Override
    public double calcVolumeForDesiredMass(double mass, double molarMass) {
        return mass / molarMass / concentration * 1000;
    }
}
