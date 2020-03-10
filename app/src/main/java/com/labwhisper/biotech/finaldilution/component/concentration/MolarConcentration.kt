package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException

class MolarConcentration(amount: Double) : Concentration(amount, ConcentrationType.MOLAR) {

    override val multiplicationFactor = 1000.0

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        if (molarMass == null) {
            throw NoMolarMassException()
        }
        return concentration * volume * molarMass / multiplicationFactor
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        if (molarMass == null) {
            throw NoMolarMassException()
        }
        return mass / molarMass / concentration * multiplicationFactor
    }
}
