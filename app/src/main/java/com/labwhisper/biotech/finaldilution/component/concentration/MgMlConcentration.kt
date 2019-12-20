package com.labwhisper.biotech.finaldilution.component.concentration

class MgMlConcentration(amount: Double) :
    Concentration(amount, ConcentrationType.MILIGRAM_PER_MILLILITER) {

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        return concentration * volume / 1000
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        return mass / concentration * 1000
    }
}
