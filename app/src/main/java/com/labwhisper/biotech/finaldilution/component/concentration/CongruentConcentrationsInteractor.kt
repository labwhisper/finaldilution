package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*

class CongruentConcentrationsInteractor {

    fun getCongruentConcentrations(
        liquid: Boolean,
        concentrationType: ConcentrationType
    ): List<ConcentrationType> {

        return when (concentrationType) {
            NX -> listOf(NX)
            MOLAR, MILIMOLAR -> listOf(MOLAR, MILIMOLAR)
            MILIGRAM_PER_MILLILITER ->
                if (liquid) listOf()
                else listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER)
            PERCENTAGE ->
                if (liquid) listOf(PERCENTAGE)
                else listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER)
        }

    }
}