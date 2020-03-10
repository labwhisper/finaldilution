package com.labwhisper.biotech.finaldilution.component.concentration

class NXConcentration(amount: Double) : Concentration(amount, ConcentrationType.NX) {

    //FIXME: Because mass is irrelevant for this concentration, there has to be another function
    // 1. Concentration shouldn't have methods it has
    // 2. Concentration with mass should have those methods
    // 3. Molar concentration should have molarMass argument
    // 4. NXConcentration should have another two methods... simple division of them

    override val multiplicationFactor = 1.0

    override fun calcDesiredMass(volume: Double, molarMass: Double?): Double {
        return concentration * volume
    }

    override fun calcVolumeForDesiredMass(mass: Double, molarMass: Double?): Double {
        return mass / concentration
    }
}
