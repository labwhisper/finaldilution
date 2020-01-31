package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*

class CompatibleConcentrationsInteractor {

    fun getCompatibleConcentrations(
        liquid: Boolean,
        concentrationType: ConcentrationType
    ): List<ConcentrationType> {
        val resultList = mutableListOf<ConcentrationType>()

        if (concentrationType == NX) {
            resultList.add(NX)
        }

        if (concentrationType in listOf(MOLAR, MILIMOLAR)) {
            resultList.add(MOLAR)
            resultList.add(MILIMOLAR)
        }

        if (!liquid && concentrationType in listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER)) {
            resultList.add(PERCENTAGE)
            resultList.add(MILIGRAM_PER_MILLILITER)
        }

        if (liquid && concentrationType == PERCENTAGE) {
            resultList.add(PERCENTAGE)
        }

        return resultList
    }
}