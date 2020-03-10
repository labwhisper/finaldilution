package com.labwhisper.biotech.finaldilution.component.concentration

class PercentageConcentration(amount: Double) :
    Concentration(amount, ConcentrationType.PERCENTAGE) {

    override val multiplicationFactor = 100.0

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        return concentration * volume / multiplicationFactor
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        return mass / concentration * multiplicationFactor
    }
}
