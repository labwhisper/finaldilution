package com.labessence.biotech.finaldilution.component.concentration

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/11/2017.
 */

class PercentageConcentration(amount: Double) : Concentration(amount, ConcentrationType.PERCENTAGE) {

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        return concentration * volume / 100
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        return mass / concentration * 100
    }
}
