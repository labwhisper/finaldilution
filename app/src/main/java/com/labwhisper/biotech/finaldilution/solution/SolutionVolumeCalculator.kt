package com.labwhisper.biotech.finaldilution.solution

import com.labwhisper.biotech.finaldilution.component.ComponentQuantityCalculator
import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException

class SolutionVolumeCalculator(
    private val componentQuantityCalculator: ComponentQuantityCalculator
) {

    fun isOverflown(solution: Solution): Boolean =
        getAllLiquidComponentsVolume(solution) > solution.volume

    private fun getAllLiquidComponentsVolume(solution: Solution): Double =
        solution.components
            .asSequence()
            .filter { it.fromStock || it.compound.liquid }
            .sumByDouble {
                try {
                    componentQuantityCalculator.getQuantity(it, solution.volume)
                } catch (e: NoMolarMassException) {
                    0.0
                }
            }

}