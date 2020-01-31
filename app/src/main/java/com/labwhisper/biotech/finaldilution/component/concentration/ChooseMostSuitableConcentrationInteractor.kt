package com.labwhisper.biotech.finaldilution.component.concentration

class ChooseMostSuitableConcentrationInteractor {

    fun chooseMostSuitableConcentration(
        currentConcentrationType: ConcentrationType,
        oppositeConcentrationType: ConcentrationType,
        suitableConcentrations: List<ConcentrationType>
    ): ConcentrationType {
        if (suitableConcentrations.contains(currentConcentrationType)) {
            return currentConcentrationType
        }
        if (suitableConcentrations.contains(oppositeConcentrationType)) {
            return oppositeConcentrationType
        }
        return calculateNearestSuitable(currentConcentrationType, suitableConcentrations)
    }

    private fun calculateNearestSuitable(
        currentConcentrationType: ConcentrationType,
        suitableConcentrations: List<ConcentrationType>
    ): ConcentrationType {
        var distanceToMostSuitable = 10
        var nearest = currentConcentrationType
        for (suitable in suitableConcentrations) {
            val newDistance = Math.abs(currentConcentrationType.value - suitable.value)
            if (newDistance < distanceToMostSuitable) {
                nearest = suitable
                distanceToMostSuitable = newDistance
            }
        }
        return nearest
    }
}

