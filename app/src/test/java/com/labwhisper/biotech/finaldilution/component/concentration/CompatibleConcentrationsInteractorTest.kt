package com.labwhisper.biotech.finaldilution.component.concentration

import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CompatibleConcentrationsInteractorTest {

    val sut = CompatibleConcentrationsInteractor()

    @Test
    fun `Solid % is compatible with % and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false, concentrationType =
            PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid mgMl is compatible with % and mgMl`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false, concentrationType =
            MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(PERCENTAGE, MILIGRAM_PER_MILLILITER), result)
    }

    @Test
    fun `Solid M is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false, concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid mM is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false, concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Solid nX is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            liquid = false, concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    @Test
    fun `Liquid % is compatible with % only`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true, concentrationType =
            PERCENTAGE
        )
        assertListsEquivalent(listOf(PERCENTAGE), result)
    }

    @Test
    fun `Liquid mgMl is compatible with nothing`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true, concentrationType =
            MILIGRAM_PER_MILLILITER
        )
        assertListsEquivalent(listOf(), result)
    }

    @Test
    fun `Liquid M is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true, concentrationType = MOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid mM is compatible with M and mM`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true, concentrationType = MILIMOLAR
        )
        assertListsEquivalent(listOf(MOLAR, MILIMOLAR), result)
    }

    @Test
    fun `Liquid nX is compatible with nX`() {
        val result = sut.getCompatibleConcentrations(
            liquid = true, concentrationType = NX
        )
        assertListsEquivalent(listOf(NX), result)
    }

    private fun assertListsEquivalent(
        expectedList: List<ConcentrationType>,
        result: List<ConcentrationType>
    ) {
        assertEquals(expectedList.size, result.size)
        assertEquals(result.containsAll(expectedList), true)
    }

}