package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*

class CompatibleConcentrationsInteractor {

    fun getCompatibleConcentrations(
        liquid: Boolean,
        molarMassGiven: Boolean,
        concentrationType: ConcentrationType
    ): List<ConcentrationType> {

        if (concentrationType == NX) {
            return listOf(NX)
        }

        if (liquid && concentrationType == MILIGRAM_PER_MILLILITER) {
            return listOf()
        }

        if (!molarMassGiven && concentrationType in listOf(MOLAR, MILIMOLAR)) {
            return listOf(MOLAR, MILIMOLAR)
        }

        val resultList = mutableListOf(PERCENTAGE)

        if (molarMassGiven) {
            resultList.add(MOLAR)
            resultList.add(MILIMOLAR)
        }

        if (!liquid) {
            resultList.add(MILIGRAM_PER_MILLILITER)
        }

        return resultList
    }
}