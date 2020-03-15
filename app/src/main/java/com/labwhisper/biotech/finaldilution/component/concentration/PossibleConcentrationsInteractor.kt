package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import com.labwhisper.biotech.finaldilution.compound.Compound

class PossibleConcentrationsInteractor {

    fun getPossibleConcentrations(compound: Compound): List<ConcentrationType> {

        val resultList = mutableListOf(PERCENTAGE)

        if (!compound.liquid) {
            resultList.add(MILIGRAM_PER_MILLILITER)
        }

        if (compound.molarMassGiven) {
            resultList.add(MOLAR)
            resultList.add(MILIMOLAR)
        }

        return resultList

    }

}
