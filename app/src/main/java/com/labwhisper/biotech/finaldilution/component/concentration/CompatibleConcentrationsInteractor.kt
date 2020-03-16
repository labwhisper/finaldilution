package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import com.labwhisper.biotech.finaldilution.compound.Compound

class CompatibleConcentrationsInteractor {

    fun getCompatibleConcentrations(
        compound: Compound,
        concentrationType: ConcentrationType
    ): List<ConcentrationType> {

        if (concentrationType == NX) {
            return listOf(NX)
        }

        if (compound.liquid && concentrationType == MILIGRAM_PER_MILLILITER) {
            return listOf()
        }

        if (!compound.molarMassGiven && concentrationType in listOf(MOLAR, MILIMOLAR)) {
            return listOf(MOLAR, MILIMOLAR)
        }

        if (compound.liquid && !compound.densityGiven
            && concentrationType in listOf(MOLAR, MILIMOLAR)
        ) {
            return listOf(MOLAR, MILIMOLAR)
        }

        if (compound.liquid && !compound.densityGiven
            && concentrationType == PERCENTAGE
        ) {
            return listOf(PERCENTAGE)
        }

        val resultList = mutableListOf(PERCENTAGE)

        if (compound.molarMassGiven) {
            resultList.add(MOLAR)
            resultList.add(MILIMOLAR)
        }

        if (!compound.liquid) {
            resultList.add(MILIGRAM_PER_MILLILITER)
        }

        return resultList
    }
}