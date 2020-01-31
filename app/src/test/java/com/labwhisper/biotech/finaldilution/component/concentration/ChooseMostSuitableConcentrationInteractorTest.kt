package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ChooseMostSuitableConcentrationInteractorTest {

    val sut = ChooseMostSuitableConcentrationInteractor()

    @Test
    fun `Choose the current concentration if it is suitable`() {
        val result = sut.chooseMostSuitableConcentration(
            currentConcentrationType = MOLAR,
            oppositeConcentrationType = MILIMOLAR,
            suitableConcentrations = listOf(MOLAR, MILIMOLAR)
        )
        Assertions.assertEquals(MOLAR, result)
    }

    @Test
    fun `Choose the opposite concentration if it is suitable and if current is not suitable`() {
        val result = sut.chooseMostSuitableConcentration(
            currentConcentrationType = PERCENTAGE,
            oppositeConcentrationType = MILIMOLAR,
            suitableConcentrations = listOf(MOLAR, MILIMOLAR)
        )
        Assertions.assertEquals(MILIMOLAR, result)
    }

    @Test
    fun `If both current and opposite are not suitable, and only one is suitable choose the one`() {
        val result = sut.chooseMostSuitableConcentration(
            currentConcentrationType = PERCENTAGE,
            oppositeConcentrationType = MILIMOLAR,
            suitableConcentrations = listOf(MILIGRAM_PER_MILLILITER)
        )
        Assertions.assertEquals(MILIGRAM_PER_MILLILITER, result)
    }

    @Test
    fun `If both current and opposite are not suitable, choose suitable nearest to current`() {
        val result = sut.chooseMostSuitableConcentration(
            currentConcentrationType = PERCENTAGE,
            oppositeConcentrationType = NX,
            suitableConcentrations = listOf(MILIMOLAR, MILIGRAM_PER_MILLILITER)
        )
        Assertions.assertEquals(MILIMOLAR, result)
    }

    @Test
    fun `If both current and opposite are not suitable, choose suitable nearest to current 2`() {
        val result = sut.chooseMostSuitableConcentration(
            currentConcentrationType = NX,
            oppositeConcentrationType = PERCENTAGE,
            suitableConcentrations = listOf(MILIMOLAR, MILIGRAM_PER_MILLILITER)
        )
        Assertions.assertEquals(MILIGRAM_PER_MILLILITER, result)
    }

}