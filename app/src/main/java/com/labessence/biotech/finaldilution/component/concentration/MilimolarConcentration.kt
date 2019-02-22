package com.labessence.biotech.finaldilution.component.concentration

import com.labessence.biotech.finaldilution.compound.NoMolarMassException

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/11/2017.
 */

class MilimolarConcentration(amount: Double) : Concentration(amount, ConcentrationType.MILIMOLAR) {

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        if (molarMass == null) {
            throw NoMolarMassException()
        }
        return concentration * volume * molarMass / 1000.0 / 1000.0
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        if (molarMass == null) {
            throw NoMolarMassException()
        }
        return mass / molarMass / concentration * 1000.0 * 1000.0
    }
}
