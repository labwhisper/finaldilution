package com.labwhisper.biotech.finaldilution.component.concentration

object ConcentrationFactory {
    fun createConcentration(type: ConcentrationType, amount: Double): Concentration {
        return when (type) {
            ConcentrationType.MOLAR -> MolarConcentration(amount)
            ConcentrationType.MILIMOLAR -> MilimolarConcentration(amount)
            ConcentrationType.PERCENTAGE -> PercentageConcentration(amount)
            ConcentrationType.MILIGRAM_PER_MILLILITER -> MgMlConcentration(amount)
            ConcentrationType.NX -> NXConcentration(amount)
        }
    }
}
