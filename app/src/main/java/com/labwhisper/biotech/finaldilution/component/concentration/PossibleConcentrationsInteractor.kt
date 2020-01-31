package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import com.labwhisper.biotech.finaldilution.compound.Compound

class PossibleConcentrationsInteractor {

    fun getPossibleConcentrations(compound: Compound) =
        getPossibleConcentrations(compound.liquid, compound.molarMass != null)

    fun getPossibleConcentrations(liquid: Boolean, molarMassGiven: Boolean):
            List<ConcentrationType> {

        val resultList = mutableListOf(PERCENTAGE)

        if (!liquid) {
            resultList.add(MILIGRAM_PER_MILLILITER)
        }

        if (molarMassGiven) {
            resultList.add(MOLAR)
            resultList.add(MILIMOLAR)
        }

        return resultList
    }

}
