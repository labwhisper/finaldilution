package com.labwhisper.biotech.finaldilution.component.concentration

class MgMlConcentration(amount: Double) :
    Concentration(amount, ConcentrationType.MILIGRAM_PER_MILLILITER) {

    override val multiplicationFactor = 1000.0

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        return concentration * volume / multiplicationFactor
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        return mass / concentration * multiplicationFactor
    }
}
