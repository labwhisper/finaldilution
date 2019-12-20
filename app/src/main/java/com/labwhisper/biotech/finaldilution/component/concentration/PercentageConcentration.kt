package com.labwhisper.biotech.finaldilution.component.concentration

class PercentageConcentration(amount: Double) :
    Concentration(amount, ConcentrationType.PERCENTAGE) {

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        return concentration * volume / 100
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        return mass / concentration * 100
    }
}
